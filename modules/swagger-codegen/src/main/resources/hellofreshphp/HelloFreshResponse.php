<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\Response;

class HelloFreshResponse extends Response implements HelloFreshResponseInterface
{
    /**
     * @var ModelInterface
     */
    protected $model;

    /**
     * @inheritdoc
     */
    public function setModel(ModelInterface $model)
    {
        $this->model = $model;
    }

    /**
     * @inheritdoc
     */
    public function getModel()
    {
        return $this->model;
    }
}
