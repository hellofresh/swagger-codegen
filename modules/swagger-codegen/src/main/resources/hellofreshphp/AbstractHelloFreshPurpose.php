<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\BaseClient\AbstractPurpose;
use HelloFresh\BaseClient\Auth\AuthProviderInterface;

abstract class AbstractHelloFreshPurpose extends AbstractPurpose
{
    const PARAM_CLIENT_ID = 'client_id';
    const PARAM_CLIENT_SECRET = 'client_secret';

    /**
     * @param  array  $parameters
     */
    public function attachClientCredentials(array &$parameters)
    {
        $authProvider = $this->client->getAuthProvider();

        if ($authProvider instanceof AuthProviderInterface) {
            $parameters = array_merge([
                self::PARAM_CLIENT_ID => $authProvider->getClientId(),
                self::PARAM_CLIENT_SECRET => $authProvider->getClientSecret(),
            ], $parameters);
        }
    }

}
