package net.kigawa.kmccore.event

interface CancelableEvent: Event {
    var cancel: Boolean
}