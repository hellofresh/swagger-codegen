<?php namespace HelloFresh\Api\PhpClient\Exception;

use HelloFresh\Api\PhpClient\HelloFreshRequest;
use HelloFresh\Api\PhpClient\HelloFreshResponse;

use Exception;

/**
 * Failed request exception
 *
 * @author    Pepijn Senders <pepijn.senders@hellofresh.de>
 * @package   hellofresh/php-client
 *
 */
class HelloFreshRequestException extends HelloFreshClientException implements HelloFreshResponse {

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
    public function __construct(HelloFreshRequest $request, $responseData, $rawResponse, $httpStatusCode)
    {
        $this->request = $request;
        $this->responseData = $responseData;
        $this->rawResponse = $rawResponse;
        $this->httpStatusCode = $httpStatusCode;

        // Try to extract message from API error
        try {
            if (property_exists($responseData, 'error')) {
                if (is_string($responseData->error)) { // OAuth package error
                    $this->message = $responseData->error_description;
                } else { // Raw framework error
                    $this->message = $responseData->error->message;
                }
            } else { // Caught framework error
                $this->message = $responseData->message;
            }
        } catch (Exception $e) {
            $this->message = 'Unable to set response message, check responseData for the API response.';
        }
    }

    /**
     * @return  HelloFresh\Api\PhpClient\HelloFreshRequest
     */
    public function getRequest()
    {
        return $this->request;
    }

    /**
     * @return  mixed
     */
    public function getResponseData()
    {
        return $this->responseData;
    }

    /**
     * @return  mixed
     */
    public function getRawResponse()
    {
        return $this->rawResponse();
    }

    /**
     * @return  int
     */
    public function getHttpStatusCode()
    {
        return $this->httpStatusCode;
    }
}
