<?php namespace HelloFresh\Api\PhpClient\Http;

interface Httpable {

  public function addRequestHeader($key, $value);

  public function getResponseHeaders();

  public function getResponseHttpStatusCode();

  public function send($url, $method = 'GET', $parameters = []);

}