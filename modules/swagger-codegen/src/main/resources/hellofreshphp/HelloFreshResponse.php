<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\Response;
use HelloFresh\HelloFreshClient\ModelInterface;

class HelloFreshResponse extends Response
{
    /**
     * @var AbstractModel
     */
    protected $model;

    /**
     * @param AbstractModel $model
     */
    public function setModel(ModelInterface $model)
    {
        $this->model = $model;
    }

    /**
     * @return AbstractModel
     */
    public function getModel()
    {
        return $this->model;
    }
}
