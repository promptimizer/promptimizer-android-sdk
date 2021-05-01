package com.haystackreviews.promptimizer

typealias OnConfigure = (Error?) -> Unit
typealias OnPromptReady = (Error?, Prompt) -> Unit
typealias OnPromptComplete = (Error?, Prompt) -> Unit