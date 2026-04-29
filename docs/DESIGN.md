---
name: Federal Study System
colors:
  surface: '#131313'
  surface-dim: '#131313'
  surface-bright: '#393939'
  surface-container-lowest: '#0e0e0e'
  surface-container-low: '#1c1b1b'
  surface-container: '#201f1f'
  surface-container-high: '#2a2a2a'
  surface-container-highest: '#353534'
  on-surface: '#e5e2e1'
  on-surface-variant: '#bfcaba'
  inverse-surface: '#e5e2e1'
  inverse-on-surface: '#313030'
  outline: '#8a9485'
  outline-variant: '#40493d'
  surface-tint: '#88d982'
  primary: '#88d982'
  on-primary: '#003909'
  primary-container: '#2e7d32'
  on-primary-container: '#cbffc2'
  inverse-primary: '#1b6d24'
  secondary: '#f8bd2a'
  on-secondary: '#402d00'
  secondary-container: '#d9a200'
  on-secondary-container: '#533c00'
  tertiary: '#ffb1c7'
  on-tertiary: '#610931'
  tertiary-container: '#b14b6f'
  on-tertiary-container: '#ffedf0'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#a3f69c'
  primary-fixed-dim: '#88d982'
  on-primary-fixed: '#002204'
  on-primary-fixed-variant: '#005312'
  secondary-fixed: '#ffdfa0'
  secondary-fixed-dim: '#f8bd2a'
  on-secondary-fixed: '#261a00'
  on-secondary-fixed-variant: '#5c4300'
  tertiary-fixed: '#ffd9e2'
  tertiary-fixed-dim: '#ffb1c7'
  on-tertiary-fixed: '#3f001c'
  on-tertiary-fixed-variant: '#7f2448'
  background: '#131313'
  on-background: '#e5e2e1'
  surface-variant: '#353534'
typography:
  headline-lg:
    fontFamily: Manrope
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Manrope
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Manrope
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Manrope
    fontSize: 10px
    fontWeight: '600'
    lineHeight: 12px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  margin-main: 1.25rem
  gutter-grid: 1rem
  stack-sm: 0.5rem
  stack-md: 1rem
  stack-lg: 1.5rem
---

## Brand & Style

This design system is built on a **Corporate / Modern** aesthetic, tailored for the gravity of the German citizenship process. It balances institutional authority with the accessibility of a modern educational tool. The UI prioritizes clarity and reduced cognitive load, using a dark environment to minimize eye strain during long study sessions.

The emotional response should be one of **confidence and stability**. By utilizing a structured layout and high-contrast primary actions, the design system guides the user through complex material without overwhelming them. The style leans into subtle "Tonal Layering" to create a sense of organized depth, ensuring that the hierarchy of information is immediately intuitive.

## Colors

The palette is rooted in the "Schwarz-Rot-Gold" heritage but adapted for a high-utility dark interface.
- **Base:** Deep Charcoal (#121212) serves as the primary canvas, providing a sophisticated backdrop that makes foreground elements pop.
- **Primary:** Emerald Green (#2E7D32) is used for high-importance actions and success states, symbolizing growth and progress.
- **Accent:** Gold (#FBC02D) is used sparingly for data visualization, badges, and highlighting critical statistics or "streaks."
- **Surfaces:** To create depth, use tiered grays. Backgrounds use the base charcoal, while containers and cards use an elevated surface color to distinguish them from the background.

## Typography

This design system utilizes **Manrope** for its exceptional legibility and modern, geometric character. It feels technical yet friendly.
- **Hierarchy:** Headlines use a tighter letter spacing and heavier weights to establish clear section breaks.
- **Legibility:** Body text is set with generous line-heights to ensure that long-form legal or historical questions remain easy to parse.
- **Data:** Labels for statistics (e.g., "0/310") should utilize medium or semibold weights to maintain visibility against dark surfaces.

## Layout & Spacing

The system employs a **Fluid Grid** model with a focus on vertical rhythm. 
- **Margins:** A standard 20px (1.25rem) side margin ensures content does not feel cramped against the bezel.
- **Density:** Spacing is generous to prevent the interface from feeling "academic" or stressful. Components are separated by a consistent 16px (1rem) gutter.
- **Grouping:** Use 8px (0.5rem) spacing for related elements within a card (e.g., an icon and its label) and 16px for distinct blocks of information.

## Elevation & Depth

Hierarchy is established through **Tonal Layers** rather than heavy shadows.
- **Level 0 (Base):** #121212. Used for the main app background.
- **Level 1 (Cards):** #1E1E1E. Used for primary content containers. These should have a very subtle 1px stroke (#2C2C2C) to define edges.
- **Level 2 (Interaction):** #2C2C2C. Used for active states, pressed buttons, or nested elements within Level 1 cards.
- **Overlays:** Navigation bars and modals use a subtle backdrop blur (15px-20px) with a semi-transparent fill to maintain context of the underlying content.

## Shapes

The design system uses a **Rounded** shape language to soften the serious nature of the content, making the app feel more approachable.
- **Standard Cards:** 1rem (16px) corner radius.
- **Small Elements:** Buttons and input fields use 0.75rem (12px) to maintain a cohesive look.
- **Selection Indicators:** Tab bar active states and pill-shaped chips use the "Full" (pill) rounding for high contrast against rectangular cards.

## Components

- **Buttons:** Primary buttons use the Emerald Green background with white or high-contrast text. Secondary buttons should be "Ghost" style with a 1px border.
- **Progress Bars:** Track backgrounds use #2C2C2C with the fill using Primary Green. Add a subtle glow or gold accent for 100% completion states.
- **Cards:** Dashboard cards should feature a consistent icon placement (top-left) within a subtle tinted square (e.g., Green icon on 10% opacity Green background).
- **List Items:** Settings and Topic lists use a horizontal layout with a trailing chevron icon to indicate drill-down capability.
- **Input Fields:** Search and text inputs should have a dark fill (#1E1E1E) and a 1px border that turns Primary Green when focused.
- **Exam HUD:** A sticky top-bar during exams should display a countdown timer using the Gold accent color to indicate urgency.