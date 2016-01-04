<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\ClientInterface;
use GuzzleHttp\Message\RequestInterface;

interface HelloFreshClientInterface extends ClientInterface
{
    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function get($url = null, $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function head($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function delete($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function put($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function patch($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function post($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function options($url = null, array $options = []);

    /**
     * @inheritdoc
     *
     * @return HelloFreshResponse
     */
    public function send(RequestInterface $request);
}
