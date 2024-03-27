/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/main/resources/**/*.{html,js}'],
  theme: {
    fontFamily: {
      "ethno": ["Ethnocentric Rg", "sans-serif"],
      "michroma": ["Michroma", "sans-serif"],
      "poppins": ["Poppins", "sans-serif"],
    },
    colors: {
      background: "#121212",
      black: "#000000",
      grey: "#7C7C7C",
      white: "#ffffff",
      whiteLight: "#F4F4F4",
      red: "#FF3B30",
      green: "#34C759",
      yellow: "#FFCC00",
      greyBlue: "#6B7888",
      lightGray: "#DFE3E8",
      lightGray200: "#D2D2D2",
      lightGray100: "#E2E2E2",
    },
    extend: {
      keyframes: {
        'slide-down': {
          '0%': { opacity: '0', transform: 'translateY(-10px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
      },
      animation: {
        'slide-down': 'slide-down 0.3s ease-out',
      },
    },
  },
  plugins: [],
};

