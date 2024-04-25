# JavaFX WebView Demo

## Description

This is a very simple JavaFX Application that shows how to load content via a JavaFX WebView and extract the HTML of that content.

The WebView controls WebEngine has an ```executeScript(scriptString)``` method that allows you to query the HTML Documents DOM via javascript.

Upon loading the document, I print out the **ids** of all elements with **ids** using this script mechanism, and this gives me a convenient method of selecting by element **id** in via my *Query* bar.

See example images for details.

## Example of Application Running
* The query bar takes a javascript query string or script.
* ![AppRunning.png](images%2FAppRunning.png)

## Example of Application Console Output
* This is where I output the results of handling script queries.
* ![AppConsole.png](images%2FAppConsole.png)