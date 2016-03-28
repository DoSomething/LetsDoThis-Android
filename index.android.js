/**
 * Starting React Native file for the news feed.
 */

'use strict';

import React, {
  AppRegistry,
} from 'react-native';

var NewsFeedView = require('./app/react/newsfeed-view');

AppRegistry.registerComponent('NewsFeedView', () => NewsFeedView);