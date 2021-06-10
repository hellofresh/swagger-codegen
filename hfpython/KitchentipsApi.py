#!/usr/bin/env python
"""
KitchentipsApi.py
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


class KitchentipsApi(object):

    def __init__(self, apiClient):
      self.apiClient = apiClient

    
    
    def getKitchenTips(self, **kwargs):
        r"""Get kitchen tips videos

        Args:
            
            country, str: Country code(s). Usage: ?country=DE,US (required)
            
            
            locale, str: Locale. Usage: ?locale=fr-BE (required)
            
            
            skip, int: Number of results to skip (required)
            
            
            take, int: Number of results to show (required)
            
            
            sort, str: Sort by field(s) in the response collection.\n *       You can do ASC and DESC by using + and -. Default is +. Ex: ?sort=price,-createdAt (required)
            
            
        
        Returns: ApiModel\Collection\Collection
        """

        allParams = ['country', 'locale', 'skip', 'take', 'sort']

        params = locals()
        for key, val in params['kwargs'].items():
            if key not in allParams:
                raise TypeError("Got an unexpected keyword argument '%s' to method getKitchenTips" % key)
            params[key] = val
        del params['kwargs']

        resourcePath = '/kitchentips'
        resourcePath = resourcePath.replace('{format}', 'json')
        method = 'GET'

        queryParams = {}
        headerParams = {}
        formParams = {}
        files = {}
        bodyParam = None

        headerParams['Accept'] = 'application/json'
        headerParams['Content-Type'] = 'application/json'

        
        if 'country' in params:
            queryParams['country'] = self.apiClient.toPathValue(params['country'])
        
        if 'locale' in params:
            queryParams['locale'] = self.apiClient.toPathValue(params['locale'])
        
        if 'skip' in params:
            queryParams['skip'] = self.apiClient.toPathValue(params['skip'])
        
        if 'take' in params:
            queryParams['take'] = self.apiClient.toPathValue(params['take'])
        
        if 'sort' in params:
            queryParams['sort'] = self.apiClient.toPathValue(params['sort'])
        

        

        

        

        

        postData = formParams if formParams else bodyParam

        response = self.apiClient.callAPI(resourcePath, method, queryParams,
                                          postData, headerParams, files=files)

        
        if not response:
            return None

        responseObject = self.apiClient.deserialize(response, r'ApiModel\Collection\Collection')
        return responseObject
        
        
        
    


