package play.api.weibo

import java.util.Date
/**
  * *****************************************
  * ************results*********************
  * *****************************************
  */

case class FollowersResult(
  users: Seq[User],
  totalNumber: Int,
  nextCursor: Int,
  previousCursor: Int)

case class PublicTimelineResult(
  statuses: List[Status])

case class RateLimitStatusResult(
  apiRateLimits: Seq[ApiRateLimit],
  ipLimit: Int,
  limitTimeUnit: String,
  remainingIpHits: Int,
  remainingUserHits: Int,
  resetTime: Date,
  resetTimeInSeconds: Int,
  userLimit: Int)

case class StatusesTimelineBatchResult(
  statuses: Seq[Status],
  previousCursor: Int,
  nextCursor: Int,
  totalNumber: Int)

case class RepostTimelineResult(
  reposts: Seq[Status],
  previousCursor: Int,
  nextCursor: Int,
  totalNumber: Int)

case class StatusesShowBatchResult(
  statuses: Seq[Either[DeletedStatus, Status]],
  totalNumber: Int)

case class StatusesMentionsResult(
  statuses: Seq[Status],
  totalNumber: Int,
  previousCursor: Int,
  nextCursor: Int
)


case class CommentsToMeResult(
  comments: Seq[Comment],
  totalNumber: Int,
  previousCursor: Int,
  nextCursor: Int
)

case class CommentsByMeResult(
  comments: Seq[Comment],
  totalNumber: Int,
  previousCursor: Int,
  nextCursor: Int
)

case class CommentsMentionsResult(
  comments: Seq[Comment],
  totalNumber: Int,
  previousCursor: Int,
  nextCursor: Int
)

case class UserTimelineResult(
  statuses: Seq[Status],
  previousCursor: Int,
  nextCursor: Int,
  totalNumber: Int
)
