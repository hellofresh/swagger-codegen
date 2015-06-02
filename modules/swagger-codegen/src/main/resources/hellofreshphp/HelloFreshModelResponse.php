<?php namespace HelloFresh\Api\PhpClient;

use DateTime;

/**
 * Model response of the API
 *
 * @author    Pepijn Senders <pepijn.senders@hellofresh.de>
 * @package   hellofresh/php-client
 *
 */
class HelloFreshModelResponse implements HelloFreshResponse {

  /**
   * @var     HelloFresh\Api\PhpClient\HelloFreshRequest $request
   */
  protected $request;

  /**
   * @var     mixed   $responseData
   */
  protected $responseData;

  /**
   * @var     mixed   $rawResponse
   */
  protected $rawResponse;

  /**
   * @var     int     $httpStatusCode
   */
  protected $httpStatusCode;

  /**
   * @param   HelloFresh\Api\PhpClient\HelloFreshRequest $request
   * @param   mixed   $responseData
   * @param   mixed   $rawResponse
   * @param   int     $httpStatusCode
   */
  public function __construct(HelloFreshRequest $request, $responseData, $rawResponse, $httpStatusCode) {
    $this->request = $request;
    $this->responseData = $responseData;
    $this->rawResponse = $rawResponse;
    $this->httpStatusCode = $httpStatusCode;
  }

  /**
   * @return  HelloFresh\Api\PhpClient\HelloFreshRequest
   */
  public function getRequest() {
    return $this->request;
  }

  /**
   * @return  mixed
   */
  public function getResponseData() {
    return $this->responseData;
  }

  /**
   * @return  mixed
   */
  public function getRawResponse() {
    return $this->rawResponse();
  }

  /**
   * @return  int
   */
  public function getHttpStatusCode() {
    return $this->httpStatusCode;
  }

  /**
   * Change the response data to serialized data
   * @param   string  $className
   * @return  void
   */
  public function deserializeData($className) {
    $data = $this->getResponseData();
    $this->recursiveAppointance($className, $data);
    $this->responseData = $data;
  }

  /**
   * Appoint variables to the raw data
   * @param   string  $className
   * @return  mixed
   */
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