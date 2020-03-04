import winsound
import time
import enum


class SoundPlayer:
    beep_type = 0
    beep_list = ["*", "beep-long.wav", "beep-short.wav"]

    @classmethod
    def beep(cls, beep_type):
        if not cls.beep_type == beep_type:
            winsound.PlaySound(cls.beep_list[beep_type], winsound.SND_ASYNC)
            cls.beep_type= beep_type
