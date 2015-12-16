<?php

namespace HelloFresh\HelloFreshClient;

use HelloFresh\HelloFreshClient\AbstractPurpose;

abstract class AbstractHelloFreshPurpose extends AbstractPurpose
{

    /**
     * @param  array  $parameters
     */
    public function attachClientCredentials(array &$parameters)
    {
        $authProvider = $this->client->getAuthProvider();

        if ($authProvider instanceof AuthProviderInterace) {
            $parameters = array_merge([
                $authProvider->getClientId(),
                $authProvider->getClientSecret(),
            ], $parameters);
        }
    }

}
