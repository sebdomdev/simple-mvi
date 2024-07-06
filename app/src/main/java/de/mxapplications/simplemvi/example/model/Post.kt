package de.mxapplications.simplemvi.example.model

import net.dean.jraw.models.Submission

data class Post(
    val submission: Submission,
    val comments: List<PostComment>
)