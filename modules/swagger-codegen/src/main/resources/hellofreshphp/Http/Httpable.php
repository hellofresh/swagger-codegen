<?php namespace HelloFresh\Api\PhpClient\Http;

/**
 * Interface for all clients
 *
 * @author    Pepijn Senders <pepijn.senders@hellofresh.de>
 * @package   hellofresh/php-client
 *
 */
interface Httpable
{

      /**
       * @param string  $key
       * @param string  $value
       * @return void
       */
      public function addRequestHeader($key, $value);

      /**
       * @return  array
       */
      public function getResponseHeaders();

      /**
       * @return  int
       */
      public function getResponseHttpStatusCode();

      /**
       * Send HTTP request with Guzzle
       * @param   string  $url
       * @param   string  $method
       * @param   array   $parameters
       * @return  mixed
       */
      public function send($url, $method = 'GET', $parameters = []);
}
