<?php

namespace HelloFresh\HelloFreshClient;

abstract class AbstractModel implements ModelInterface
{
    /**
     * @param array $data
     */
    public function __construct(array $data = [])
    {
        foreach ($data as $key => $value) {
            $this[$key] = $value;
        }
    }

    /**
     * @return array
     */
    public function toArray()
    {
        $entity = [];

        foreach (self::$swaggerTypes as $name => $type) {
            if (starts_with($name, 'HelloFresh\\HelloFreshClient\\Entities') && class_exists($name)) {
                $entity[$name] = $this->$name->toArray();
            } else {
                $entity[$name] = $this->$name;
            }
        }

        return $entity;
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
