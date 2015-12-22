<?php

namespace HelloFresh\HelloFreshClient;

use Illuminate\Support\Collection as IlluminateCollection;

abstract class AbstractCollection extends IlluminateCollection implements ModelInterface
{
    /**
     * Storage of items
     *
     * @var array $items
     */
    public $items = [];

    /**
     * Create Collection entity with array of keys with corresponding values
     *
     * @param array $data
     */
    public function __construct(array $data = [])
    {
        $this->items = array_key_exists('items', $data) ?
            (array) $data['items'] :
            [];
    }
}
