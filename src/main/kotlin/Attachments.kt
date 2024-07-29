fun main() {
    WallService.add(Post(1, 1, 1, date = 1))
    WallService.add(Post(2, 1, 1, date = 1))
    WallService.print()
    println(WallService.update(Post(2, 1, 1, date = 1, likes = Likes(30))))
    println(WallService.update(Post(3, 1, 1, date = 1, likes = Likes(70))))
    WallService.print()
    println(WallService.createComment(1, Comment()))
    println(WallService.createComment(3, Comment()))
}

class PostNotFoundException(message: String) : RuntimeException(message)
data class Post(
    val id: Int,
    val ownerId: Int,
    val fromID: Int,
    val date: Int,
    val text: String = "Hello!",
    var views: Int = 0,
    val friendsOnly: Boolean = true,
    val canDelete: Boolean = false,
    val canEdit: Boolean = false,
    val isFavorite: Boolean = false,
    val likes: Likes = Likes(),
    val comments: Comments? = null,
    val reposts: Reposts = Reposts(),
    val copyright: Copyright? = null,
    val attachments: Array<Attachment> = arrayOf (
        PhotoAttachment(Photo()),
        AudioAttachment(Audio()),
        VideoAttachment(Video()),
        DocAttachment(Doc()),
        GraffitiAttachment(Graffiti())
    )
)

data class Likes(
    var count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

data class Comments(
    var count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true,
    val canClose: Boolean = true,
    val canOpen: Boolean = true
)

data class Reposts(
    var count: Int = 0,
    val userReposted: Boolean = false
)

data class Comment(
    val id: Int = 1,
    val fromId: Int = 1,
    val date: Int = 1,
    val text: String = "text",
    val replyToUser: Int? = null,
    val replyToComment: Int? = null
)

data class Copyright(
    val id: Int = 0,
    val link: String = "link",
    val name: String = "name",
    val type: String = "type"
)

interface Attachment {
    val type: String
}

data class Photo(
    val id: Int = 1,
    val albumId: Int = 1,
    val ownerId: Int = 1,
    val userId: Int = 1,
    val text: String = "text",
    val date: Int = 1,
    )

data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "photo"
}

data class Video(
    val id: Int = 1,
    val ownerId: Int = 1,
    val title: String = "text",
    val date: Int = 1,
    val views: Int = 1,
    val comments: Int = 1,
)

data class VideoAttachment(val video: Video) : Attachment {
    override val type = "video"
}

data class Audio(
    val id: Int = 1,
    val ownerId: Int = 1,
    val artist: String = "text",
    val title: String = "text",
    val date: Int = 1,
)

data class AudioAttachment(val audio: Audio) : Attachment {
    override val type = "audio"
}

data class Doc(
    val id: Int = 1,
    val ownerId: Int = 1,
    val title: String = "text",
    val size: Int = 1,
    val url: String = "text",
    val date: Int = 1,
    val type: Int = 1
)

data class DocAttachment(val doc: Doc) : Attachment {
    override val type = "doc"
}

data class Graffiti(
    val id: Int = 1,
    val ownerId: Int = 1,
    val photo_130: String = "text",
    val photo_604: String = "text"
)

data class GraffitiAttachment(val graffiti: Graffiti) : Attachment {
    override val type = "graffiti"
}

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var lastId = 0
    fun add(post: Post): Post {
        posts += post.copy(id = ++lastId, likes = post.likes.copy())
        return posts.last()
    }

    fun update(newPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == newPost.id) {
                posts[index] = newPost.copy(likes = newPost.likes.copy())
                return true
            }
        }
        return false
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        for (post in posts)
            if (post.id == postId) {
                comments += comment
                return comment
            }
        throw PostNotFoundException("Post not found with id $postId")
    }

    fun clear() {
        posts = emptyArray()
        lastId = 0
    }

    fun print() {
        for (post in posts) {
            print(post)
            println(' ')
        }
    }
}