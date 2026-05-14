export const COLORS = {
  primary: '#6366F1',
  primaryLight: '#818CF8',
  primaryDark: '#4F46E5',
  
  // Priority Colors
  high: '#EF4444',
  medium: '#F59E0B',
  low: '#10B981',
  
  // Note Colors
  noteYellow: '#FEF3C7',
  noteBlue: '#DBEAFE',
  noteGreen: '#D1FAE5',
  notePink: '#FCE7F3',
  notePurple: '#EDE9FE',
  
  // Status
  success: '#10B981',
  warning: '#F59E0B',
  error: '#EF4444',
  
  // UI
  background: '#F9FAFB',
  card: '#FFFFFF',
  text: '#1F2937',
  textSecondary: '#6B7280',
  textLight: '#9CA3AF',
  border: '#E5E7EB',
  shadow: '#000000',
  
  // Dark Mode
  darkBackground: '#111827',
  darkCard: '#1F2937',
  darkText: '#F9FAFB',
  darkTextSecondary: '#9CA3AF',
};

export const PRIORITIES = {
  HIGH: 'high',
  MEDIUM: 'medium',
  LOW: 'low',
};

export const PRIORITY_LABELS = {
  [PRIORITIES.HIGH]: 'High',
  [PRIORITIES.MEDIUM]: 'Medium',
  [PRIORITIES.LOW]: 'Low',
};

export const NOTE_COLORS = [
  { id: 'yellow', color: COLORS.noteYellow, name: 'Yellow' },
  { id: 'blue', color: COLORS.noteBlue, name: 'Blue' },
  { id: 'green', color: COLORS.noteGreen, name: 'Green' },
  { id: 'pink', color: COLORS.notePink, name: 'Pink' },
  { id: 'purple', color: COLORS.notePurple, name: 'Purple' },
];

export const DEFAULT_FOLDERS = [
  { id: 'work', name: 'Work', icon: 'briefcase', color: '#3B82F6' },
  { id: 'personal', name: 'Personal', icon: 'person', color: '#8B5CF6' },
  { id: 'ideas', name: 'Ideas', icon: 'bulb', color: '#F59E0B' },
  { id: 'important', name: 'Important', icon: 'star', color: '#EF4444' },
];

export const FILTERS = {
  ALL: 'all',
  ACTIVE: 'active',
  COMPLETED: 'completed',
  PINNED: 'pinned',
  FAVORITES: 'favorites',
};
