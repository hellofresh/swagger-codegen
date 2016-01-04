<?php

namespace HelloFresh\HelloFreshClient;

abstract class AbstractModel implements ModelInterface
{
    /**
     * @var array
     */
    public static $swaggerTypes = [];

    /**
     * @param array $data
     */
    public function __construct(array $data = [])
    {
        foreach ($data as $key => $value) {
            $this->$key = $value;
        }
    }

    /**
     * @inheritdoc
     */
    public function toArray()
    {
        $entityArray = [];

        foreach (static::$swaggerTypes as $name => $type) {
            if ($this->$name instanceof ModelInterface) {
                $entityArray[$name] = $this->$name->toArray();
            } else {
                $entityArray[$name] = $this->$name;
            }
        }

        return $entityArray;
    }

    /**
     * @param  integer $options
     * @return string
     */
    public function toJson($options = 0)
    {
        return json_encode($this->toArray(), $options);
    }
}
