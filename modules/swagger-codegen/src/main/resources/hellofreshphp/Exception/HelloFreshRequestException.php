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

    try {
      if (property_exists($responseData, 'error')) {
        $this->message = $responseData->error_description;
      } else {
        $this->message = $responseData->message;
      }
    } catch (Exception $e) {
      $this->message = 'Unable to set response message, check responseData for the API response.';
    }
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