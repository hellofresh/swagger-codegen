<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\Response;

class HelloFreshResponse extends Response
{
    /**
     * @var AbstractModel
     */
    protected $model;

    /**
     * @param AbstractModel $model
     */
    public function setModel(AbstractModel $model)
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
