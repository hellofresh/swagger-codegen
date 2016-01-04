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
     * @param  string $className
     * @param  object $data
     * @return ModelInterface
     */
    protected static function recursiveAppointance($className, $data)
    {
        if (strpos($className, 'array[') === 0) {
            $subClassName = substr($className, 6, -1);
            $values = [];

            foreach ($data as $value) {
                $values[] = static::recursiveAppointance($subClassName, $value);
            }

            $data = $values;
        } elseif ($className === 'DateTime') {
            $data = new \DateTime($data);
        } elseif (in_array($className, static::$types)) {
            $value = (is_object($data) || is_array($data)) ? json_encode($data) : $data;
            settype($value, $className);

            $data = $value;
        } elseif (class_exists($className)) {
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

            $data = $instance;
        }

        return $data;
    }

}
