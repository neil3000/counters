from manim import *

class rn3(ZoomedScene):
    def construct(self):
        #self.camera.background_color = WHITE

        scallop = SVGMobject("scallop.svg").scale(3.5)
        logo = ImageMobject("logo.png").scale(1.15)

        self.add(scallop, logo)
        self.play(Rotate(scallop, angle=2*PI/15.0, run_time=3, rate_func=linear))