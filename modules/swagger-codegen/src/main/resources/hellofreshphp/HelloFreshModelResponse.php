<?php namespace HelloFresh\Api\PhpClient;

use DateTime;

class HelloFreshModelResponse implements HelloFreshResponse {

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

  public function deserializeData($className, $data = null) {
    $data = $this->getResponseData();
    $this->recursiveAppointance($className, $data);
    $this->responseData = $data;
  }

  public function recursiveAppointance($className, &$data) {
    if (strpos($className, 'array[') === 0) {
      $subClassName = substr($className, 6, -1);

      $values = [];
      foreach ($data as $value) {
        $values[] = $this->recursiveAppointance($subClassName, $value);
      }

      $data = $values;
    } else if ($className === 'DateTime') {
      $data = new DateTime($data);
    } else if (in_array($className, ['string', 'int', 'float', 'bool'])) {
      $value = (is_object($data) || is_array($data)) ? json_encode($data) : $data;
      settype($value, $className);

      $data = $value;
    } else if (class_exists($className)) {
      $instance = new $className;

      foreach ($className::$swaggerTypes as $property => $type) {
        if (isset($data->$property)) {
          $instance->$property = $this->recursiveAppointance($type, $data->$property);
        }
      }

      $data = $instance;
    }

    return $data;
  }

}