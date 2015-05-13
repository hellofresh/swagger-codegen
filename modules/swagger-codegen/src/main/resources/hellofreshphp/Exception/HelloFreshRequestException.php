<?php namespace HelloFresh\Api\PhpClient\Exception;

use HelloFresh\Api\PhpClient\HelloFreshRequest;
use HelloFresh\Api\PhpClient\HelloFreshResponse;

class HelloFreshRequestException extends HelloFreshClientException implements HelloFreshResponse {

  protected $request;
  protected $responseData;
  protected $rawResponse;
  protected $httpStatusCode;

  public function __construct(HelloFreshRequest $request, $responseData, $rawResponse, $httpStatusCode) {
    $this->request = $request;
    $this->responseData = $responseData;
    $this->rawResponse = $rawResponse;
    $this->httpStatusCode = $httpStatusCode;
  }

  public function getRequest() {
    return $this->request;
  }

  public function getResponseData() {
    return $this->responseData;
  }

  public function getRawResponse() {
    return $this->rawResponse();
  }

  public function getHttpStatusCode() {
    return $this->httpStatusCode;
  }

}