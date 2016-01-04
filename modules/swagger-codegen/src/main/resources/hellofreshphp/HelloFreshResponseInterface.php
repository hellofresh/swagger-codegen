<?php

namespace HelloFresh\HelloFreshClient;

use GuzzleHttp\Message\ResponseInterface;

interface HelloFreshResponseInterface extends ResponseInterface
{

    /**
     * @param ModelInterface $model
     */
    public function setModel(ModelInterface $model);

    /**
     * @return ModelInterface $model
     */
    public function getModel();

}
