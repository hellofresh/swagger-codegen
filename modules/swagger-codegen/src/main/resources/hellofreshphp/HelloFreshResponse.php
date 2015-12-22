<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\Response;
use HelloFresh\HelloFreshClient\ModelInterface;

class HelloFreshResponse extends Response
{
    /**
     * @var ModelInterface
     */
    protected $model;

    /**
     * @param ModelInterface $model
     */
    public function setModel(ModelInterface $model)
    {
        $this->model = $model;
    }

    /**
     * @return ModelInterface
     */
    public function getModel()
    {
        return $this->model;
    }
}
