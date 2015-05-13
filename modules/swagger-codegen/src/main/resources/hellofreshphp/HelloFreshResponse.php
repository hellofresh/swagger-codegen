<?php namespace HelloFresh\Api\PhpClient;

interface HelloFreshResponse {

  public function __construct(HelloFreshRequest $request, $responseData, $rawResponse, $httpStatusCode);

  public function getRequest();
  public function getResponseData();
  public function getRawResponse();
  public function getHttpStatusCode();

}