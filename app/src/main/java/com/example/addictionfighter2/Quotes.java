package com.example.addictionfighter2;

import java.util.Random;

public class Quotes {

    private String[] customMessages = {
            "Believe in change, it starts now",
            "Embrace your strength, defeat your struggles",
            "One step at a time moves mountains",
            "Break free, your spirit deserves peace",
            "Find strength in every challenge",
            "Courage is not the absence of fear",
            "Hope is stronger than addiction",
            "Let each day be a fresh start",
            "Change is possible, believe in yourself",
            "Overcome, rise, and shine",
            "Freedom from addiction is a choice",
            "Claim your life back, step by step",
            "Your will is stronger than your cravings",
            "Embrace the struggle, cherish the victory",
            "Let go of what holds you back",
            "Seek peace, not escape",
            "Today's efforts are tomorrow's rewards",
            "Your journey, your pace, your victory",
            "Transform your obstacles into stepping stones",
            "Rise above, one decision at a time",
            "Believe you can and you're halfway there.",
            "You are stronger than you think.",
            "Dream big and dare to fail.",
            "Strive for progress, not perfection.",
            "The only way to do great work is to love what you do.",
            "Success is not final, failure is not fatal: It is the courage to continue that counts.",
            "In the middle of every difficulty lies opportunity.",
            "Don't wait for opportunity, create it.",
            "Your limitation—it's only your imagination.",
            "The harder you work for something, the greater you'll feel when you achieve it.",
            "Wake up with determination. Go to bed with satisfaction.",
            "The only person you should try to be better than is the person you were yesterday.",
            "You don't have to be great to start, but you have to start to be great.",
            "The future belongs to those who believe in the beauty of their dreams.",
            "Believe you deserve it and the universe will serve it."
    };

    private Random randomGenerator;

    public Quotes() {
        randomGenerator = new Random();
    }

    public String getQuote() {
        int index = randomGenerator.nextInt(customMessages.length);
        return customMessages[index];
    }
}

