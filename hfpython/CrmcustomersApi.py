#!/usr/bin/env python
"""
CrmcustomersApi.py
Copyright 2015 Reverb Technologies, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

NOTE: This class is auto generated by the swagger code generator program. Do not edit the class manually.
"""
import sys
import os
from urllib.parse import quote

from .models import *


class CrmcustomersApi(object):

    def __init__(self, apiClient):
      self.apiClient = apiClient

    
    
    def getAppboyData(self, **kwargs):
        r"""Get customers data for CRM Appboy

        Args:
            
            customersIds, str: List of customer IDs, Ex: 1,2,3 (required)
            
            
            country, str: Country code(s). Usage: ?country=DE,US (required)
            
            
        
        Returns: ApiModel\CRM\Appboy\CustomerCollection
        """

        allParams = ['customersIds', 'country']

        params = locals()
        for key, val in params['kwargs'].items():
            if key not in allParams:
                raise TypeError("Got an unexpected keyword argument '%s' to method getAppboyData" % key)
            params[key] = val
        del params['kwargs']

        resourcePath = '/crm/customers/appboy'
        resourcePath = resourcePath.replace('{format}', 'json')
        method = 'GET'

        queryParams = {}
        headerParams = {}
        formParams = {}
        files = {}
        bodyParam = None

        headerParams['Accept'] = 'application/json'
        headerParams['Content-Type'] = 'application/json'

        
        if 'customersIds' in params:
            queryParams['customersIds'] = self.apiClient.toPathValue(params['customersIds'])
        
        if 'country' in params:
            queryParams['country'] = self.apiClient.toPathValue(params['country'])
        

        

        

        

        

        postData = formParams if formParams else bodyParam

        response = self.apiClient.callAPI(resourcePath, method, queryParams,
                                          postData, headerParams, files=files)

        
        if not response:
            return None

        responseObject = self.apiClient.deserialize(response, r'ApiModel\CRM\Appboy\CustomerCollection')
        return responseObject
        
        
        
    

