<?php namespace HelloFresh\Api\PhpClient;

use HelloFresh\Api\PhpClient\Http\Httpable;
use HelloFresh\Api\PhpClient\Http\GuzzleClient;
use HelloFresh\Api\PhpClient\Http\CurlClient;
use HelloFresh\Api\PhpClient\Exception\HelloFreshRequestException;
use HelloFresh\Api\PhpClient\Exception\HelloFreshParameterException;

class HelloFreshRequest {

  public static $httpClientHandler;
  public static $baseUrl;
  public static $accessToken;

  protected $path;
  protected $method;
  protected $parameters;

  public function __construct($path, $method = 'GET', $parameters = []) {
    $this->path = $path;
    $this->method = $method;
    $this->parameters = $parameters;
  }

  public function execute() {
    $url = $this->getRequestUrl();
    $parameters = $this->getParameters();

    $url = self::substituteValues($url, $parameters);

    if ($this->method === 'GET') {
      $url = self::addParamsToUrl($url, $parameters);
      $this->parameters = [];
    }

    $accessToken = self::getAccessToken();

    $connection = self::getHttpClientHandler();
    $connection->addRequestHeader('User-Agent', 'HelloFresh\\Api\\PhpClient');
    $connection->addRequestHeader('Accept-Encoding', '*');
    $connection->addRequestHeader('Accept', 'application/json');

    if ($accessToken) {
      $connection->addRequestHeader('Authorization', "Bearer $accessToken");
    }

    $result = $connection->send($url, $this->method, $parameters);
    $headers = $connection->getResponseHeaders();
    $httpStatusCode = $connection->getResponseHttpStatusCode();

    $decodedResult = json_decode($result);

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

  public function getRequestUrl() {
    return self::$baseUrl . $this->path;
  }

  public function getParameters() {
    return $this->parameters;
  }

  public static function setBaseUrl($url) {
    static::$baseUrl = $url;
  }

  public static function getBaseUrl() {
    return static::$baseUrl;
  }

  public static function setAccessToken($accessToken) {
    static::$accessToken = $accessToken;
  }

  public static function getAccessToken() {
    return static::$accessToken;
  }

  public static function addParamsToUrl($url, &$params = []) {
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

  public static function substituteValues($url, &$params = []) {
    return preg_replace_callback('/\{(.*?)\}/', function($matches) use (&$params) {
      $parameter = $matches[1];

      if (!array_key_exists($parameter, $params)) {
        throw new HelloFreshParameterException("Missing parameter $parameter.");
      }

      $value = $params[$parameter];
      unset($params[$parameter]);

      return $value;
    }, $url);
  }

  public static function setHttpClientHandler(Httpable $handler) {
    static::$httpClientHandler = $handler;
  }

  public static function getHttpClientHandler() {
    if (static::$httpClientHandler) {
      return static::$httpClientHandler;
    }
    return function_exists('curl_init') ? new CurlClient : new GuzzleClient;
  }

}