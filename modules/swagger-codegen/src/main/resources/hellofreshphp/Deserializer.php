<?php

namespace HelloFresh\HelloFreshClient;

use GuzzleHttp\Message\ResponseInterface;

class Deserializer
{

    public static function deserialize($fqcn, ResponseInterface &$response)
    {
        dd(func_get_args());
    }

}
