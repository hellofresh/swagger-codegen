<?php namespace HelloFresh\Api\PhpClient;

interface HelloFreshResponse {

  /**
   * @param   HelloFresh\Api\PhpClient\HelloFreshRequest $request
   * @param   mixed   $responseData
   * @param   mixed   $rawResponse
   * @param   int     $httpStatusCode
   */
  public function __construct(HelloFreshRequest $request, $responseData, $rawResponse, $httpStatusCode);

  /**
   * @return  HelloFresh\Api\PhpClient\HelloFreshRequest
   */
  public function getRequest();

  /**
   * @return  mixed
   */
  public function getResponseData();

  /**
   * @return  mixed
   */
  public function getRawResponse();

  /**
   * @return  int
   */
  public function getHttpStatusCode();

}