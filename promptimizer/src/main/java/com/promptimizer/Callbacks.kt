package com.promptimizer

typealias onComplete = (Error?) -> Unit
typealias OnPromptReady = (Error?, Prompt) -> Unit
typealias OnPromptComplete = (Error?, Prompt) -> Unit