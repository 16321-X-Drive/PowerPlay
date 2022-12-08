package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.TelemetryPacket

data class Pose(var position: Point, var heading: Double)

fun TelemetryPacket.drawRobot(pose: Pose, target: Boolean = false) {
    val tl = pose.position + RobotConstants.TL.rotated(pose.heading)
    val tri1 = pose.position + Point(-1.0, RobotConstants.TOP_HEIGHT).rotated(pose.heading)
    val tri2 = pose.position + Point(0.0, RobotConstants.TOP_HEIGHT + 1.0).rotated(pose.heading)
    val tri3 = pose.position + Point(1.0, RobotConstants.TOP_HEIGHT).rotated(pose.heading)
    val tr = pose.position + RobotConstants.TR.rotated(pose.heading)
    val bl = pose.position + RobotConstants.BL.rotated(pose.heading)
    val br = pose.position + RobotConstants.BR.rotated(pose.heading)

    val canvas = this.fieldOverlay()
    canvas.setStroke(if (target) "pink" else "gold")
    canvas.setStrokeWidth(2)
    canvas.strokeCircle(pose.position.x, pose.position.y, 2.0)
    canvas.strokePolygon(tl, tr, br, bl)

    if (!target) {
        val s1 = pose.position + RobotConstants.LOW_SENSOR_OFFSET.rotated(pose.heading)
        val s2 = pose.position + RobotConstants.HIGH_SENSOR_OFFSET.rotated(pose.heading)

        canvas.setStroke("blue")
        canvas.setStrokeWidth(1)
        canvas.strokeCircle(s1.x, s1.y, RobotConstants.SENSOR_RADIUS)
        canvas.strokeCircle(s2.x, s2.y, RobotConstants.SENSOR_RADIUS)
    }
}
