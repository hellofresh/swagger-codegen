<?php namespace HelloFresh\Api\PhpClient\Http;

use HelloFresh\Api\PhpClient\Exception\HelloFreshClientException;
use HelloFresh\Api\PhpClient\Http\HelloFreshCurl;

use CURLFile;

class CurlClient implements Httpable {

  protected $requestHeaders = [];
  protected $responseHeaders = [];
  protected $responseHttpStatusCode = 0;
  protected $curlErrorMessage = '';
  protected $curlErrorCode = '';
  protected $rawResponse;
  protected $helloFreshCurl;
  protected static $disableIPv6;

  const CURL_PROXY_QUIRK_VER = 0x071E00;
  const CONNECTION_ESTABLISHED = "HTTP/1.0 200 Connection established\r\n\r\n";

  public function __construct(HelloFreshCurl $helloFreshCurl = null) {
    $this->helloFreshCurl = $helloFreshCurl ?: new HelloFreshCurl;
    self::$disableIPv6 = self::$disableIPv6 ?: false;
  }

  public static function disableIPv6() {
    self::$disableIPv6 = true;
  }

  public function addRequestHeader($key, $value) {
    $this->requestHeaders[$key] = $value;
  }

  public function getResponseHeaders() {
    return $this->responseHeaders;
  }

  public function getResponseHttpStatusCode() {
    return $this->responseHttpStatusCode;
  }

  public function send($url, $method = 'GET', $parameters = []) {
    $this->openConnection($url, $method, $parameters);
    $this->tryToSendRequest();

    if ($this->curlErrorCode) {
      throw new HelloFreshClientException($this->curlErrorMessage, $this->curlErrorCode);
    }

    list($rawHeaders, $rawBody) = $this->extractResponseHeadersAndBody();

    $this->responseHeaders = self::headersToArray($rawHeaders);

    $this->closeConnection();

    return $rawBody;
  }

  public function openConnection($url, $method = 'GET', $parameters) {
    $options = [
    CURLOPT_URL => $url,
    CURLOPT_CONNECTTIMEOUT => 10,
    CURLOPT_TIMEOUT => 60,
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_HEADER => true,
    CURLOPT_SSL_VERIFYHOST => 2,
    CURLOPT_SSL_VERIFYPEER => true,
    ];

    if ($method !== 'GET') {
      $options[CURLOPT_POSTFIELDS] = !$this->paramsHaveFile($parameters) ? http_build_query($parameters, null, '&') : $parameters;
    }

    if ($method === 'DELETE' || $method === 'PUT') {
      $options[CURLOPT_CUSTOMREQUEST] = $method;
    }

    if (count($this->requestHeaders) > 0) {
      $options[CURLOPT_HTTPHEADER] = $this->compileRequestHeaders();
    }

    if (self::$disableIPv6) {
      $options[CURLOPT_IPRESOLVE] = CURL_IPRESOLVE_V4;
    }

    $this->helloFreshCurl->init();
    $this->helloFreshCurl->setopt_array($options);
  }

  public function closeConnection() {
    $this->helloFreshCurl->close();
  }

  public function tryToSendRequest() {
    $this->sendRequest();
    $this->curlErrorMessage = $this->helloFreshCurl->error();
    $this->curlErrorCode = $this->helloFreshCurl->errno();
    $this->responseHttpStatusCode = $this->helloFreshCurl->getinfo(CURLINFO_HTTP_CODE);
  }

  public function sendRequest() {
    $this->rawResponse = $this->helloFreshCurl->exec();
  }

  public function compileRequestHeaders() {
    foreach ($this->requestHeaders as $key => $value) {
      $return[] = $key . ': ' . $value;
    }

    return $return;
  }

  public function extractResponseHeadersAndBody() {
    $headerSize = self::getHeaderSize();
    $rawHeaders = mb_substr($this->rawResponse, 0, $headerSize);
    $rawBody = mb_substr($this->rawResponse, $headerSize);
    return [
    trim($rawHeaders),
    trim($rawBody)
    ];
  }

  public static function headersToArray($rawHeaders) {
    $rawHeaders = str_replace("\r\n", "\n", $rawHeaders);
    $headerCollection = explode("\n\n", trim($rawHeaders));
    $rawHeader = array_pop($headerCollection);

    $headerComponents = explode("\n", $rawHeader);
    foreach ($headerComponents as $line) {
      if (strpos($line, ': ') === false) {
        $headers['http_code'] = $line;
      } else {
        list ($key, $value) = explode(': ', $line);
        $headers[$key] = $value;
      }
    }
    return $headers;
  }

  private function getHeaderSize() {
    $headerSize = $this->helloFreshCurl->getinfo(CURLINFO_HEADER_SIZE);
    if ( $this->needsCurlProxyFix() ) {
      if (preg_match('/Content-Length: (\d+)/', $this->rawResponse, $m)) {
        $headerSize = mb_strlen($this->rawResponse) - $m[1];
      } elseif (stripos($this->rawResponse, self::CONNECTION_ESTABLISHED) !== false) {
        $headerSize += mb_strlen(self::CONNECTION_ESTABLISHED);
      }
    }
    return $headerSize;
  }

  private function needsCurlProxyFix() {
    $ver = $this->helloFreshCurl->version();
    $version = $ver['version_number'];

    return $version < self::CURL_PROXY_QUIRK_VER;
  }

  private function paramsHaveFile(array $params) {
    foreach ($params as $value) {
      if ($value instanceof CURLFile) {
        return true;
      }
    }

    return false;
  }

}