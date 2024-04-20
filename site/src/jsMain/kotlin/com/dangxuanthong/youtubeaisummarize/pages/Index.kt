package com.dangxuanthong.youtubeaisummarize.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.title
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
@Suppress("unused")
fun HomePage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Span(
            attrs = Modifier
                .fontSize(2.em)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            SpanText(
                text = "THIS PAGE IS UNDER CONSTRUCTION 🚧",
                modifier = Modifier.fontWeight(FontWeight.Bold)
            )
            Br()
            Text(value = "Check my current work ")
            Link(
                text = "on Github!",
                path = "https://github.com/DangXuanThong/YoutubeAISummarize/tree/kobweb",
                modifier = Modifier.title("Go to Github.com"),
                openExternalLinksStrategy = OpenLinkStrategy.IN_PLACE
            )
        }
    }
}
