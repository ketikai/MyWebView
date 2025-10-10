package pers.ketikai.lsp.mywebview.hook

interface Hooker {

    val name: String
        get() = this::class.java.name
}