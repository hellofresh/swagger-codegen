<?php namespace HelloFresh\Api\PhpClient\Http;

use HelloFresh\Api\PhpClient\Exception\HelloFreshClientException;

use GuzzleHttp\Client;
use GuzzleHttp\Exception\AdapterException;
use GuzzleHttp\Exception\RequestException;

class GuzzleClient implements Httpable {

  protected $requestHeaders = [];
  protected $responseHeaders = [];
  protected $responseHttpStatusCode = 0;
  protected static $guzzleClient;

  public function __construct(Client $guzzleClient = null) {
    self::$guzzleClient = $guzzleClient ?: new Client;
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