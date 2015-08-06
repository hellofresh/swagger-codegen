<?php namespace HelloFresh\Api\PhpClient;

use HelloFresh\Api\PhpClient\Http\Httpable;
use HelloFresh\Api\PhpClient\Http\GuzzleClient;
use HelloFresh\Api\PhpClient\Http\CurlClient;
use HelloFresh\Api\PhpClient\Exception\HelloFreshRequestException;
use HelloFresh\Api\PhpClient\Exception\HelloFreshParameterException;

use Illuminate\Contracts\Cache\Store;
use Illuminate\Cache\ArrayStore;

/**
 * Base Request to the HelloFresh API
 *
 * @author    Pepijn Senders <pepijn.senders@hellofresh.de>
 * @package   hellofresh/php-client
 *
 */
class HelloFreshRequest
{

    const CLIENT_ID = 'HelloFreshApiPhpClient';

    /**
     * @var   HelloFresh\Api\PhpClient\Http\Httpable $httpClientHandler
     */
    public static $httpClientHandler;

    /**
     * @var   string  $baseUrl
     */
    public static $baseUrl;

    /**
     * @var   string  $accessToken
     */
    public static $accessToken;

    /**
     * @var   Illuminate\Contracts\Cache\Store $storable
     */
    public static $storable;

    /**
     * @var   string  $path
     */
    protected $path;

    /**
     * @var   string  $method
     */
    protected $method;

    /**
     * @var   array   $parameters
     */
    protected $parameters;

    /**
     * @var   array   $cacheOptions
     */
    protected $cacheOptions = [
        'expire' => 30, // minutes
        'refresh' => true,
    ];

    /**
     * @param   string  $path
     * @param   string  $method
     * @param   array   $parameters
     * @param   array   $cacheOptions
     * @return  void
     */
    public function __construct($path, $method = 'GET', $parameters = [], $cacheOptions = [])
    {
        $this->path = $path;
        $this->method = $method;
        $this->parameters = $parameters;
        $this->cacheOptions = array_merge($this->cacheOptions, $cacheOptions);
    }

    /**
     * Execute the request
     * @return  HelloFresh\Api\PhpClient\HelloFreshResponse
     * @throws  HelloFresh\Api\PhpClient\Exception\ClientException
     */
    public function execute()
    {
        $url = $this->getRequestUrl();
        $parameters = $this->getParameters();

        $url = self::substituteValues($url, $parameters);

        if ($this->method === 'GET') {
            $url = self::addParamsToUrl($url, $parameters);
            $this->parameters = [];
        }

        $accessToken = self::getAccessToken();

        $connection = self::getHttpClientHandler();
        $connection->addRequestHeader('User-Agent', self::CLIENT_ID);
        $connection->addRequestHeader('Accept-Encoding', '*');
        $connection->addRequestHeader('Accept', 'application/json');

        if ($accessToken) {
            $connection->addRequestHeader('Authorization', "Bearer $accessToken");
        }

        list($result, $headers, $httpStatusCode, $decodedResult) = $this->speak($connection, $url, $parameters);

        if (is_null($decodedResult)) {
            $out = [];
            parse_str($result, $out);

            throw new HelloFreshRequestException($this, $out, $result, $httpStatusCode);
        }

        if ($httpStatusCode >= 200 && $httpStatusCode < 300) {
            return new HelloFreshModelResponse($this, $decodedResult, $result, $httpStatusCode);
        } else {
            throw new HelloFreshRequestException($this, $decodedResult, $result, $httpStatusCode);
        }
    }

    /**
     * Create an identifier for the cached results
     * @param   array   $arguments
     * @return  string
     */
    public static function cacheIdentifier($arguments)
    {
        return self::CLIENT_ID . '-' . md5(json_encode($arguments));
    }

    /**
     * Speak with the API or speak with the Cache repo
     * @param   HelloFresh\Api\PhpClient\Http\Httpable $connection
     * @param   string  $url
     * @param   array   $parameters
     * @return  array
     */
    public function speak(Httpable $connection, $url, $parameters)
    {
        $identifier = self::cacheIdentifier(func_get_args());
        $storable = self::getStorable();

        if (!$this->cacheOptions['refresh'] && !is_null($storable->get($identifier))) {
            return $storable->get($identifier);
        }

        $result = $connection->send($url, $this->method, $parameters);
        $headers = $connection->getResponseHeaders();
        $httpStatusCode = $connection->getResponseHttpStatusCode();

        $decodedResult = json_decode($result);

        $response = [
            $result,
            $headers,
            $httpStatusCode,
            $decodedResult,
        ];

        if (!$this->cacheOptions['refresh'] && $this->cacheOptions['expire'] > 0) {
            $storable->put($identifier, $response, $this->cacheOptions['expire']);
        }

        return $response;
    }

    /**
     * @return  string
     */
    public function getRequestUrl()
    {
        return self::$baseUrl . $this->path;
    }

    /**
     * @return  array
     */
    public function getParameters()
    {
        return $this->parameters;
    }

    /**
     * @param   string  $url
     * @return  void
     */
    public static function setBaseUrl($url)
    {
        static::$baseUrl = $url;
    }

    /**
     * @return  string
     */
    public static function getBaseUrl()
    {
        return static::$baseUrl;
    }

    /**
     * @param   string  $accessToken
     * @return  void
     */
    public static function setAccessToken($accessToken)
    {
        static::$accessToken = $accessToken;
    }

    /**
     * @return  string
     */
    public static function getAccessToken()
    {
        return static::$accessToken;
    }

    /**
     * @param   string  $url
     * @param   array   &$params
     * @return  string
     */
    public static function addParamsToUrl($url, &$params = [])
    {
        if (count($params) <= 0) {
            return $url;
        }

        if (strpos($url, '?') === false) {
            return $url . '?' . http_build_query($params, null, '&');
        }

        list($path, $queryString) = explode('?', $url, 2);
        parse_str($queryString, $queryArray);

        $queryParams = array_merge($params, $queryArray);
        $params = [];

        return $path . '?' . http_build_query($queryParams, null, '&');
    }

    /**
     * @param   string  $url
     * @param   array   $params
     * @return  string
     */
    public static function substituteValues($url, &$params = [])
    {
        return preg_replace_callback('/\{(.*?)\}/', function ($matches) use (&$params) {
            $parameter = $matches[1];

            if (!array_key_exists($parameter, $params)) {
                throw new HelloFreshParameterException("Missing parameter $parameter.");
            }

            $value = $params[$parameter];
            unset($params[$parameter]);

            return $value;
        }, $url);
    }

    /**
     * @param   HelloFresh\Api\PhpClient\Http\Httpable $handler
     * @return  void
     */
    public static function setHttpClientHandler(Httpable $handler)
    {
        static::$httpClientHandler = $handler;
    }

    /**
     * @return  HelloFresh\Api\PhpClient\Http\Httpable
     */
    public static function getHttpClientHandler()
    {
        if (static::$httpClientHandler) {
            return static::$httpClientHandler;
        }

        return function_exists('curl_init') ? new CurlClient : new GuzzleClient;
    }

    /**
     * @param   Illuminate\Cache\Store $handler
     * @return  void
     */
    public static function setStorable(Store $handler)
    {
        static::$storable = $handler;
    }

    /**
     * @return  Illuminate\Cache\Store
     */
    public static function getStorable()
    {
        if (static::$storable) {
            return static::$storable;
        }

        static::$storable = new ArrayStore;

        return static::$storable;
    }
}
