<?php namespace HelloFresh\Api\PhpClient\Http;

use HelloFresh\Api\PhpClient\Exception\HelloFreshClientException;

use GuzzleHttp\Client;
use GuzzleHttp\Exception\AdapterException;
use GuzzleHttp\Exception\RequestException;

class GuzzleClient implements Httpable {

  /**
   * @var   array   $requestHeaders
   */
  protected $requestHeaders = [];

  /**
   * @var   array   $responseHeaders
   */
  protected $responseHeaders = [];

  /**
   * @var   int     $responseHttpStatusCode
   */
  protected $responseHttpStatusCode = 0;

  /**
   * @var   GuzzleHttp\Client $guzzleClient
   */
  protected static $guzzleClient;

  /**
   * @param GuzzleHttp\Client $guzzleClient
   * @return void
   */
  public function __construct(Client $guzzleClient = null) {
    self::$guzzleClient = $guzzleClient ?: new Client;
  }

  /**
   * @param string  $key
   * @param string  $value
   * @return void
   */
  public function addRequestHeader($key, $value) {
    $this->requestHeaders[$key] = $value;
  }

  /**
   * @return  array
   */
  public function getResponseHeaders() {
    return $this->responseHeaders;
  }

  /**
   * @return  int
   */
  public function getResponseHttpStatusCode() {
    return $this->responseHttpStatusCode;
  }

  /**
   * Send HTTP request with Guzzle
   * @param   string  $url
   * @param   string  $method
   * @param   array   $parameters
   * @return  mixed
   */
  public function send($url, $method = 'GET', $parameters = []) {
    if (count($parameters)) {
      $options = [
        'body' => $parameters,
      ];
    } else {
      $options = [];
    }

    $request = self::$guzzleClient->createRequest($method, $url, $options);

    foreach ($this->requestHeaders as $headerName => $headerValue) {
      $request->setHeader($headerName, $headerValue);
    }

    try {
      $rawResponse = self::$guzzleClient->send($request);
    } catch (RequestException $e) {
      if ($e->getPrevious() instanceof AdapterException) {
        throw new HelloFreshClientException($e->getMessage(), $e->getCode());
      }

      $rawResponse = $e->getResponse();
    }

    $this->responseHttpStatusCode = $rawResponse->getStatusCode();
    $this->responseHeaders = $rawResponse->getHeaders();

    return $rawResponse->getBody();
  }

}