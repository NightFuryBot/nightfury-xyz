/*
 * Copyright 2018 Kaidan Gustave
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

// Foreword on this:
// This fucking webpack bullshit took me a cumulative 16 hours to figure out
// and that was before any actual code writing, not even including the dozens
// of revisions I made to it while fine tuning it to work as best as I could
// for building to production vs development, outputting to more conveniently
// organized directories, and generally stuff that any sane programming language
// I could have done in a couple of hours.
// -_-

// Change to false to create dev builds when running 'npm run webpack'
// Change to true to create production builds when running 'npm run webpack'
// And no, you cannot use "npm run webpack -p" don't even fucking try it.
const production = false;

// Resources
const path = require('path');
const webpack = require('webpack');
const CleanCSS = require('clean-css');

// Plugins
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackIncludeAssetsPlugin = require('html-webpack-include-assets-plugin');
const KotlinWebpackPlugin = require('@jetbrains/kotlin-webpack-plugin');
const MinifyJsWebpackPlugin = require('babel-minify-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const FaviconsWebpackPlugin = require('favicons-webpack-plugin');

// Paths
const buildDir = 'build';
const buildDirPath = path.join(__dirname, `${buildDir}`);
const kotlinBuildDir = 'kotlin_build';
const kotlinBuildPath = path.resolve(__dirname, kotlinBuildDir);

// Other Constants
const styleSheets = ['index.css', 'projects.css']; // These should be in src/styles
const cssMinifier = new CleanCSS();

function minifyContent(content) {
  // Building for production
  if(production) {
    const minified = cssMinifier.minify(content);
    const errors = minified.errors;

    if(errors.length === 0) {
      return minified.styles;
    } else {
      // Minifying failed, we will print errors
      for(let error in errors) {
        console.error(error);
      }

      return content;
    }
  } else {
    // No minifying required, we are building for development
    return content;
  }
}

function htmlPlugin(filename, title, output = undefined) {
  return new HtmlWebpackPlugin({
    filename: filename,
    title: title,
    output: output === undefined? filename : output,
    template: path.join(__dirname, 'public', filename)
  });
}

// Initialize plugins
// We do this outside of the module.exports because
// in production we add some extra plugins (see below)
const webpackPlugins = [
  new CleanWebpackPlugin([buildDir, kotlinBuildDir], !production? { // !NANI?
    // Do not regenerate favicons in development builds
    exclude: ['favicons', 'favicons.json', 'fonts']
  } : {}),

  new KotlinWebpackPlugin({
    src: __dirname + '/src',
    verbose: false,
    optimize: production,
    moduleName: 'kotlin-app',
    output: kotlinBuildPath,
    metaInfo: false,
    sourceMaps: true,
    libraries: [
      require.resolve('@jetbrains/kotlin-extensions'),
      require.resolve('@jetbrains/kotlin-react'),
      require.resolve('@jetbrains/kotlin-react-dom'),
      require.resolve('kotlinx-html')
    ]
  }),

  new CopyWebpackPlugin([
    {
      from: path.join('src', 'styles'), to: 'css',
      transform: function (content) { return minifyContent(content) }
    },
    { from: path.join('public', '.htaccess') },
    {
      from: path.join('public', 'fonts'), to: 'fonts',
      transform: function (content, path) {
        if(/^[\S\s]+\.css$/.test(path)) {
          return minifyContent(content)
        } else {
          return content
        }
      }
    }
  ]),

  htmlPlugin('index.html', 'NightFury'),

  htmlPlugin('projects.html', 'Projects'),

  new HtmlWebpackIncludeAssetsPlugin({
    assets: styleSheets.map(function (s) { return "css/" + s }),
    append: false,
    // TODO hash: production // Hash the assets in production
  }),

  new FaviconsWebpackPlugin({
    logo: './public/images/nightfury.jpg',
    emitStats: true,
    inject: true,
    statsFilename: 'favicons.json',
    prefix: 'favicons/',
    persistentCache: !production,
    icons: {
      twitter: true,
      windows: true
    }
  })
];

if(production) {
  webpackPlugins.push(new webpack.DefinePlugin({
    'process.env.NODE_ENV': JSON.stringify('production')
  }));

  // Minify JS for production
  webpackPlugins.push(new MinifyJsWebpackPlugin());
}

module.exports = {
  entry: 'kotlin-app',

  devServer: {
    index: 'index.html',
    watchContentBase: true,
    contentBase: path.join(__dirname, buildDir)
  },

  resolve: {
    alias: {
      // Alias src/styles to just styles
      styles: path.join(__dirname, 'src', 'styles'),
      logging: path.join(__dirname, 'src', 'logging'),
      colors: path.join(__dirname, 'node_modules', 'colors')
    },
    modules: ['node_modules', kotlinBuildDir]
  },

  module: {
    loaders: [
      {
        loaders: ['style-loader', 'css-loader'],
        test: /\.css$/,
        exclude: '/node_modules/',
        options: {
          minimize: production,
          sourceMap: true
        }
      },
      {
        loader: 'babel-loader',
        test: /\.js$/,
        exclude: '/node_modules/'
      }
    ],
    rules: [
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.js$/,
        include: path.resolve(__dirname, `../${kotlinBuildDir}`),
        /*exclude: [
          /kotlin\.js$/  // Kotlin runtime doesn't have sourcemaps at the moment
        ],*/
        use: ['babel-loader', 'react-loader', 'source-map-loader'],
        enforce: 'pre'
      }
    ]
  },

  output: {
    path: buildDirPath,
    filename: production? 'build.min.js' : 'build.js'
  },

  plugins: webpackPlugins
};
