package com.sun40.robotumblr.model;

import com.sun40.robotumblr.TumblrExtras;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 04.09.15 9:57.
 */
public class RawPost {

    /**
     * he short name used to uniquely identify a blog
     */
    public String blog_name;//
    /**
     * The post's unique ID
     */
    public long id;//
    /**
     * The location of the post
     */
    public String post_url;//
    /**
     * The type of post
     * {@link TumblrExtras.Post#TEXT}
     * {@link TumblrExtras.Post#QUOTE}
     * {@link TumblrExtras.Post#LINK}
     * {@link TumblrExtras.Post#ANSWER}
     * {@link TumblrExtras.Post#VIDEO}
     * {@link TumblrExtras.Post#AUDIO}
     * {@link TumblrExtras.Post#PHOTO}
     * {@link TumblrExtras.Post#CHAT
     */
    public String type;//
    /**
     * The GMT date and time of the post, as a string
     */
    public String date;//
    /**
     * The time of the post, in seconds since the epoch
     */
    public long timestamp;//
    /**
     * The post format: html or markdown
     * {@link TumblrExtras.Format#FORMAT_HTML}
     * {@link TumblrExtras.Format#FORMAT_MARKDOWN}
     */
    public String format;//
    /**
     * Indicates the current state of the post
     * States are published, queued, draft and private
     * {@link TumblrExtras.State#PUBLISHED}
     * {@link TumblrExtras.State#QUEUED}
     * {@link TumblrExtras.State#DRAFT}
     * {@link TumblrExtras.State#PRIVATE}
     */
    public String state;//
    /**
     * The key used to reblog this post
     */
    public String reblog_key;//
    /**
     * Tags applied to the post
     */
    public String[] tags;//
    /**
     * Count of notes does not exist in
     * {@link TumblrExtras.Post#ANSWER} post types
     */
    @PostType(type = {TumblrExtras.Post.TEXT,
            TumblrExtras.Post.VIDEO,
            TumblrExtras.Post.QUOTE,
            TumblrExtras.Post.LINK,
            TumblrExtras.Post.ANSWER,
            TumblrExtras.Post.AUDIO,
            TumblrExtras.Post.CHAT,
            TumblrExtras.Post.PHOTO})
    public int note_count;//
    /**
     * Indicates whether the post was created via the Tumblr bookmarklet
     * Exists only if true
     */
    public boolean bookmarklet;//
    /**
     * Indicates whether the post was created via mobile/email publishing
     * Exists only if true
     */
    public boolean mobile;//
    /**
     * The URL for the source of the content (for quotes, reblogs, etc.)
     * Exists only if there's a content source
     */
    public String source_url;//
    /**
     * The title of the source site
     * Exists only if there's a content source
     */
    public String source_title;//

    //Different blog type variables

    /**
     * The optional title of the post
     * presents in
     * {@link TumblrExtras.Post#TEXT}
     */
    @PostType(type = {TumblrExtras.Post.TEXT, TumblrExtras.Post.LINK, TumblrExtras.Post.CHAT})
    public String title;// //

    /**
     * The full post body
     * presents in
     * {@link TumblrExtras.Post#TEXT}
     */
    @PostType(type = {TumblrExtras.Post.TEXT, TumblrExtras.Post.CHAT})
    public String body;//

    /**
     * Photo objects with properties
     * presents in
     * {@link TumblrExtras.Post#PHOTO}
     */
    @PostType(type = {TumblrExtras.Post.PHOTO, TumblrExtras.Post.LINK})
    public Photo[] photos; //
    /**
     * The user-supplied caption
     * presents in
     * {@link TumblrExtras.Post#PHOTO}
     */
    @PostType(type = {TumblrExtras.Post.PHOTO, TumblrExtras.Post.AUDIO, TumblrExtras.Post.VIDEO})
    public String caption;
    /**
     * reblogged data
     */
    public Reblog reblog;
    /**
     * The text of the quote (can be modified by the user when posting)
     */
    @PostType(type = {TumblrExtras.Post.QUOTE})
    public String text;//
    /**
     * Full HTML for the source of the quote
     * Example: <a href="...">Steve Jobs</a>
     */
    @PostType(type = {TumblrExtras.Post.QUOTE})
    public String source;//
    /**
     * The link
     */
    @PostType(type = {TumblrExtras.Post.LINK})
    public String url; //
    /**
     * The author of the article the link points to
     */
    @PostType(type = {TumblrExtras.Post.LINK})
    public String author;//
    /**
     * An excerpt from the article the link points to
     */
    @PostType(type = {TumblrExtras.Post.LINK})
    public String excerpt;//
    /**
     * The publisher of the article the link points to
     */
    @PostType(type = {TumblrExtras.Post.LINK})
    public String publisher;//
    /**
     * A user-supplied description
     */
    @PostType(type = {TumblrExtras.Post.LINK})
    public String description;//
    /**
     * Array of objects with the following properties:
     * name – string: name of the speaker
     * label – string: label of the speaker
     * phrase – string: text
     */
    @PostType(type = {TumblrExtras.Post.CHAT})
    public Dialogue[] dialogue;
    /**
     * Number of times the audio post has been played
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public int plays;
    /**
     * audio url
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String audio_url;
    /**
     * audio source url
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String audio_source_url;
    /**
     * is audio external
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public boolean is_external;
    /**
     * Audio source type
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String audio_type;
    /**
     * Location of the audio file's ID3 album art image
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String album_art;
    /**
     * The audio file's ID3 artist value
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String artist;
    /**
     * The audio file's ID3 album value
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String album;
    /**
     * The audio file's ID3 title value
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public String track_name;
    /**
     * The audio file's ID3 track value
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public int track_number;
    /**
     * The audio file's ID3 year value
     */
    @PostType(type = {TumblrExtras.Post.AUDIO})
    public int year;
    /**
     * video url
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public String videoUrl;
    /**
     * Video thumbnail url
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public String thumbnail_url;
    /**
     * Video thumbnail width
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public int thumbnail_width;
    /**
     * Video thumbnail height
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public int thumbnail_height;
    /**
     * Video duration in seconds
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public int duration;
    /**
     * Video type use:
     * {@link com.sun40.robotumblr.TumblrExtras.Video#TUMBLR}
     * {@link com.sun40.robotumblr.TumblrExtras.Video#YOUTUBE}
     * {@link com.sun40.robotumblr.TumblrExtras.Video#VIMEO}
     * {@link com.sun40.robotumblr.TumblrExtras.Video#VINE}
     * {@link com.sun40.robotumblr.TumblrExtras.Video#UNKNOWN}
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public String video_type;
    /**
     * Placement id for vine video {@link com.sun40.robotumblr.TumblrExtras.Video#VINE}
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public String placement_id;
    /**
     * Placement id for vine video {@link com.sun40.robotumblr.TumblrExtras.Video#YOUTUBE} and {@link com.sun40.robotumblr.TumblrExtras.Video#VIMEO}}
     */
    @PostType(type = {TumblrExtras.Post.VIDEO})
    public String permalink_url;
    /**
     * The blog name of the user asking the question
     */
    @PostType(type = {TumblrExtras.Post.ANSWER})
    public String asking_name; //
    /**
     * The blog URL of the user asking the question
     */
    @PostType(type = {TumblrExtras.Post.ANSWER})
    public String asking_url; //
    /**
     * The question being asked
     */
    @PostType(type = {TumblrExtras.Post.ANSWER})
    public String question; //
    /**
     * The answer given
     */
    @PostType(type = {TumblrExtras.Post.ANSWER})
    public String answer; //


    //Queued RawPost
    public String recommended_source;
    public String recommended_color;
    public Long scheduled_publish_time;
    public String queued_state;

    /**
     * Image permalink for Photo post
     */
    public String image_permalink;
    /**
     * Photo post link url
     */
    public String link_url;
    /**
     * Notes data
     */
    public Note[] notes;
    /**
     * Blog & Post trail data
     */
    public Trail[] trail;


    public long reblogged_from_id;
    public String reblogged_from_url;
    public String reblogged_from_name;
    public String reblogged_from_title;
    public long reblogged_root_id;
    public String reblogged_root_url;
    public String reblogged_root_name;
    public String reblogged_root_title;

    public @interface PostType {
        @TumblrExtras.PostType String[] type();
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "RawPost{" + separator +
                "\tblog_name='" + blog_name + '\'' + separator +
                "\tid=" + id + separator +
                "\tpost_url='" + post_url + '\'' + separator +
                "\ttype='" + type + '\'' + separator +
                "\tdate='" + date + '\'' + separator +
                "\ttimestamp=" + timestamp + separator +
                "\tformat='" + format + '\'' + separator +
                "\tstate='" + state + '\'' + separator +
                "\treblog_key='" + reblog_key + '\'' + separator +
                "\ttags=" + Arrays.toString(tags) + separator +
                "\tnote_count=" + note_count + separator +
                "\tbookmarklet=" + bookmarklet + separator +
                "\tmobile=" + mobile + separator +
                "\tsource_url='" + source_url + '\'' + separator +
                "\tsource_title='" + source_title + '\'' + separator +
                "\ttitle='" + title + '\'' + separator +
                "\tbody='" + body + '\'' + separator +
                "\tphotos=" + Arrays.toString(photos) + separator +
                "\tcaption='" + caption + '\'' + separator +
                "\treblog=" + reblog + separator +
                "\ttext='" + text + '\'' + separator +
                "\tsource='" + source + '\'' + separator +
                "\turl='" + url + '\'' + separator +
                "\tauthor='" + author + '\'' + separator +
                "\texcerpt='" + excerpt + '\'' + separator +
                "\tpublisher='" + publisher + '\'' + separator +
                "\tdescription='" + description + '\'' + separator +
                "\tdialogue=" + Arrays.toString(dialogue) + separator +
                "\tplays=" + plays + separator +
                "\taudio_url='" + audio_url + '\'' + separator +
                "\taudio_source_url='" + audio_source_url + '\'' + separator +
                "\tis_external=" + is_external + separator +
                "\taudio_type='" + audio_type + '\'' + separator +
                "\talbum_art='" + album_art + '\'' + separator +
                "\tartist='" + artist + '\'' + separator +
                "\talbum='" + album + '\'' + separator +
                "\ttrack_name='" + track_name + '\'' + separator +
                "\ttrack_number=" + track_number + separator +
                "\tyear=" + year + separator +
                "\tvideoUrl='" + videoUrl + '\'' + separator +
                "\tthumbnail_url='" + thumbnail_url + '\'' + separator +
                "\tthumbnail_width=" + thumbnail_width + separator +
                "\tthumbnail_height=" + thumbnail_height + separator +
                "\tduration=" + duration + separator +
                "\tvideo_type='" + video_type + '\'' + separator +
                "\tplacement_id='" + placement_id + '\'' + separator +
                "\tpermalink_url='" + permalink_url + '\'' + separator +
                "\tasking_name='" + asking_name + '\'' + separator +
                "\tasking_url='" + asking_url + '\'' + separator +
                "\tquestion='" + question + '\'' + separator +
                "\tanswer='" + answer + '\'' + separator +
                "\trecommended_source='" + recommended_source + '\'' + separator +
                "\trecommended_color='" + recommended_color + '\'' + separator +
                "\tscheduled_publish_time=" + scheduled_publish_time + separator +
                "\tqueued_state='" + queued_state + '\'' + separator +
                "\timage_permalink='" + image_permalink + '\'' + separator +
                '}';
    }


    public Post create() {

        switch (type) {
            case TumblrExtras.Post.TEXT:
                return new TextPost(this);

            case TumblrExtras.Post.QUOTE:
                return new QuotePost(this);

            case TumblrExtras.Post.LINK:
                return new LinkPost(this);

            case TumblrExtras.Post.PHOTO:
                return new PhotoPost(this);

            case TumblrExtras.Post.CHAT:
                return new ChatPost(this);

            case TumblrExtras.Post.ANSWER:
                return new AnswerPost(this);

            case TumblrExtras.Post.AUDIO:
                return new AudioPost(this);

            case TumblrExtras.Post.VIDEO:
                return new VideoPost(this);

            default:
                return new Post(this) {
                };
        }
    }

    public static class Reblog {
        public String tree_html;
        public String comment;

        @Override
        public String toString() {
            String separator = System.getProperty("line.separator");
            return "Reblog{" + separator +
                    "\ttree_html='" + tree_html + '\'' + separator +
                    "\tcomment='" + comment + '\'' + separator +
                    '}';
        }
    }
}
