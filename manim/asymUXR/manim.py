from manim import *

class SquareToCircle(ZoomedScene):
    def construct(self):
        self.camera.background_color = WHITE

        dp4 = ImageMobject("4dp.png")
        dp8 = ImageMobject("8dp.png")
        textOption1 = Text("Option A", color=BLUE).shift(LEFT*5.3 + DOWN*5).scale(.3)
        textOption2 = Text("Option B", color=ORANGE).shift(LEFT*5.3 + DOWN*5).scale(.3)
        lineLeft = Line((-3.27, -5.35, 0), (-3.27 - .31, -5.35, 0), color=GREEN)
        lineOption1 = Line((-2.75, -5.64, 0), (-2.75, -5.64 - .24, 0), color=BLUE)
        lineOption2 = Line((-2.75, -5.64, 0), (-2.75, -5.64 - .24 - .07, 0), color=ORANGE)

        self.add(dp8)
        self.add(dp4)

        self.camera.frame.scale(2.5)
        textGroup = VGroup()
        textGroup.add(Text("Neïl Rahmouni", color=BLACK))
        textGroup.add(Text("App: Counters | nrah.fr/counters", color=BLACK).scale(.5))
        textGroup.arrange(DOWN, center=False, aligned_edge=LEFT)  
        self.add(textGroup.align_to(self.camera.frame, DL).shift(UP*.5+RIGHT*.5))

        self.play(self.camera.frame.animate.scale(.15).shift(LEFT*3.5 + DOWN*5))
        self.wait(1)

        self.play(Create(lineLeft))
        self.play(
            FadeIn(textOption1),
            Create(lineOption1)
        )
        self.wait(2)

        self.play(
            FadeOut(dp4),
            FadeTransform(lineOption1, lineOption2),
            FadeTransform(textOption1, textOption2)
        )
        self.wait(2)

        self.play(
            FadeOut(lineLeft),
            FadeOut(lineOption2)
        )
        self.play(
            FadeIn(dp4),
            FadeTransform(textOption2, textOption1)
        )
        self.wait(1)

        self.play(
            FadeOut(dp4),
            FadeTransform(textOption1, textOption2)
        )
        self.wait(2)

        self.play(
            FadeOut(textOption2),
            self.camera.frame.animate.scale(1/.15).shift(RIGHT*3.5 + UP*5).scale(.5).shift(DOWN*3),
            FadeIn(dp4)
        )

        text1 = Text("A", color=BLUE).shift(RIGHT*7.73+DOWN*3)
        text2 = Text("B", color=ORANGE).shift(LEFT*7.73+DOWN*3)
        self.play(
            dp4.animate.shift(RIGHT*2),
            dp8.animate.shift(LEFT*2),
        )
        self.play(
            FadeIn(text1),
            FadeIn(text2)
        )
        self.wait(4)

        self.play(
            self.camera.frame.animate.scale(2).shift(UP*3),
            dp4.animate.shift(LEFT*2),
            dp8.animate.shift(RIGHT*2),
            FadeOut(text1),
            FadeOut(text2),
        )
        self.wait(2)


        