package com.example.namumovil.model

class Request (s: String, s1: String, s2: String) {
    private var to: String? = s
    private var subject: String? = s1
    private var content: String? = s2

    fun getTo(): String? {
        return to
    }

    fun setTo(to: String?) {
        this.to = to
    }

    fun getSubject(): String? {
        return subject
    }

    fun setSubject(subject: String?) {
        this.subject = subject
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }
}