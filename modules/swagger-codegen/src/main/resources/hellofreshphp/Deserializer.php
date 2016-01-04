<?php

namespace HelloFresh\HelloFreshClient;

class Deserializer
{
    /**
     * @var array
     */
    protected static $types = [
        'boolean',
        'bool',
        'integer',
        'int',
        'float',
        'double',
        'string',
        'array',
        'object',
        'null'
    ];

    /**
     * @param  string            $fqcn
     * @param  HelloFreshResponse $response
     * @return ModelInterface
     */
    public static function deserialize($fqcn, HelloFreshResponse $response)
    {
        return static::recursiveAppointance($fqcn, json_decode($response->getBody()));
    }

    /**
     * @param string $className
     * @param object $data
     *
     * @return mixed
     */
    protected static function recursiveAppointance($className, $data)
    {
        if (strpos($className, 'array[') === 0) {
            /* @var $data array */

            return self::deserializeArray($className, $data);
        }

        if ($className === 'DateTime') {
            return self::deserializeDateTime($data);
        }

        if (in_array($className, static::$types)) {
            return self::deserializeBasic($className, $data);
        }

        if (class_exists($className)) {
            return self::deserializeObject($className, $data);
        }

        return $data;
    }

    /**
     * @param string $className
     * @param array $data
     *
     * @return array
     */
    protected function deserializeArray($className, $data)
    {
        $subClassName = substr($className, 6, -1);
        $values = [];

        foreach ($data as $value) {
            $values[] = static::recursiveAppointance($subClassName, $value);
        }

        return $values;
    }

    /**
     * @param $data
     *
     * @return \DateTime
     */
    protected function deserializeDateTime($data)
    {
        return new \DateTime($data);
    }

    /**
     * @param string $className
     * @param mixed $data
     *
     * @return mixed
     */
    protected function deserializeBasic($className, $data)
    {
        $value = (is_object($data) || is_array($data)) ? json_encode($data) : $data;
        settype($value, $className);

        return $value;
    }

    /**
     * @param string $className
     * @param object $data
     *
     * @return object
     */
    protected function deserializeObject($className, $data)
    {
        /* @var $className \HelloFresh\HelloFreshClient\AbstractModel */

        $instance = new $className;

        foreach ($className::$swaggerTypes as $property => $type) {
            if ($property === 'items') {
                return new $className(static::recursiveAppointance($type, $data->$property));
            }

            if (isset($data->$property)) {
                $instance->$property = static::recursiveAppointance($type, $data->$property);
            }
        }

        return $instance;
    }
}
