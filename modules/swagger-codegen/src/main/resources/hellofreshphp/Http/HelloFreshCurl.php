<?php namespace HelloFresh\Api\PhpClient\Http;

/**
 * cURL function wrapper
 *
 * @author    Pepijn Senders <pepijn.senders@hellofresh.de>
 * @package   hellofresh/php-client
 *
 */
class HelloFreshCurl
{

    /**
     * @var   resource
     */
    protected $curl = null;

    /**
     * @return void
     */
    public function init()
    {
        if ($this->curl === null) {
            $this->curl = curl_init();
        }
    }

    /**
     * @param string  $key
     * @param string  $value
     * @return void
     */
    public function setopt($key, $value)
    {
        curl_setopt($this->curl, $key, $value);
    }

    /**
     * @param array   $options
     * @return void
     */
    public function setopt_array(array $options)
    {
        curl_setopt_array($this->curl, $options);
    }

    /**
     * @return mixed
     */
    public function exec()
    {
        return curl_exec($this->curl);
    }

    /**
     * @return int
     */
    public function errno()
    {
        return curl_errno($this->curl);
    }

    /**
     * @return string
     */
    public function error()
    {
        return curl_error($this->curl);
    }

    /**
     * @return mixed
     */
    public function getinfo($type)
    {
        return curl_getinfo($this->curl, $type);
    }

    /**
     * @return array
     */
    public function version()
    {
        return curl_version();
    }

    /**
     * @return void
     */
    public function close()
    {
        curl_close($this->curl);
        $this->curl = null;
    }
}
