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
const gulp = require('gulp');
const clean = require('gulp-clean');

// Cleans all .js files from the build directory
gulp.task('clean-js', () => gulp.src('build/**/*.js', {read: false}).pipe(clean()));

// Cleans all .css files from the build directory
gulp.task('clean-css', () => gulp.src('build/css/', {read: false}).pipe(clean()));

// Cleans all .html files from the build directory
gulp.task('clean-html', () => gulp.src('build/**/*.html', {read: false}).pipe(clean()));

// Cleans all kotlin2js libraries
gulp.task('clean-kotlin-build', () => gulp.src('kotlin_build/', {read: false}).pipe(clean()));

// Cleans the build directory optimized for the next development build
gulp.task('clean-dev', ['clean-js', 'clean-css', 'clean-html', 'clean-kotlin-build']);

// Cleans all build files and kotlin2js libraries
gulp.task('clean-all', ['clean-kotlin-build'], () => gulp.src('build/', {read: false}).pipe(clean()));

// Default task
gulp.task('default', ['clean-dev']);