import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useNotes } from '../context/NotesContext';
import { COLORS, PRIORITIES, PRIORITY_LABELS, NOTE_COLORS, DEFAULT_FOLDERS } from '../constants/theme';
import ColorPicker from '../components/ColorPicker';
import PriorityPicker from '../components/PriorityPicker';
import FolderPicker from '../components/FolderPicker';
import DateTimePicker from '../components/DateTimePicker';
import SubtaskInput from '../components/SubtaskInput';
import LockToggle from '../components/LockToggle';

export default function AddNoteScreen({ navigation }) {
  const { addNote, verifyPin, isPinSet } = useNotes();
  
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState(PRIORITIES.MEDIUM);
  const [noteColor, setNoteColor] = useState('yellow');
  const [folder, setFolder] = useState(null);
  const [dueDate, setDueDate] = useState(null);
  const [reminder, setReminder] = useState(null);
  const [subtasks, setSubtasks] = useState([]);
  const [isLocked, setIsLocked] = useState(false);
  const [pin, setPin] = useState('');
  const [showPinInput, setShowPinInput] = useState(false);

  const handleSave = async () => {
    if (!title.trim()) {
      Alert.alert('Error', 'Please enter a title');
      return;
    }

    if (isLocked && pin.length !== 4) {
      Alert.alert('Error', 'Please enter a 4-digit PIN');
      return;
    }

    const noteData = {
      title: title.trim(),
      description: description.trim(),
      priority,
      color: noteColor,
      folder,
      dueDate,
      reminder,
      subtasks,
      isLocked,
    };

    if (isLocked && pin) {
      noteData.pin = pin; // Will be hashed in context
    }

    await addNote(noteData);
    navigation.goBack();
  };

  const handleLockToggle = async (value) => {
    if (value && isPinSet) {
      setShowPinInput(true);
      setIsLocked(true);
    } else if (value && !isPinSet) {
      Alert.alert(
        'Set Global PIN First',
        'Please set a global PIN in Settings before locking notes.',
        [{ text: 'OK' }]
      );
      setIsLocked(false);
    } else {
      setIsLocked(false);
      setPin('');
    }
  };

  const handlePinSubmit = async () => {
    if (pin.length !== 4) {
      Alert.alert('Error', 'PIN must be 4 digits');
      return;
    }
    const isValid = await verifyPin(pin);
    if (isValid) {
      setShowPinInput(false);
    } else {
      Alert.alert('Error', 'Invalid PIN');
      setPin('');
    }
  };

  return (
    <SafeAreaView style={styles.container} edges={['bottom']}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={styles.keyboardView}
      >
        <ScrollView
          style={styles.scrollView}
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
        >
          {/* Title Input */}
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Title *</Text>
            <TextInput
              style={[styles.titleInput, { backgroundColor: NOTE_COLORS.find(c => c.id === noteColor)?.color || COLORS.noteYellow }]}
              placeholder="Enter note title..."
              placeholderTextColor={COLORS.textSecondary}
              value={title}
              onChangeText={setTitle}
              maxLength={100}
            />
          </View>

          {/* Description Input */}
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Description</Text>
            <TextInput
              style={[styles.descriptionInput, { backgroundColor: NOTE_COLORS.find(c => c.id === noteColor)?.color || COLORS.noteYellow }]}
              placeholder="Write your note here..."
              placeholderTextColor={COLORS.textSecondary}
              value={description}
              onChangeText={setDescription}
              multiline
              textAlignVertical="top"
              maxLength={5000}
            />
            <Text style={styles.charCount}>{description.length}/5000</Text>
          </View>

          {/* Priority Picker */}
          <PriorityPicker
            selectedPriority={priority}
            onSelectPriority={setPriority}
          />

          {/* Color Picker */}
          <ColorPicker
            selectedColor={noteColor}
            onSelectColor={setNoteColor}
          />

          {/* Folder Picker */}
          <FolderPicker
            folders={DEFAULT_FOLDERS}
            selectedFolder={folder}
            onSelectFolder={setFolder}
          />

          {/* Due Date Picker */}
          <DateTimePicker
            label="Due Date"
            value={dueDate}
            onChange={setDueDate}
            minDate={new Date()}
          />

          {/* Reminder Picker */}
          <DateTimePicker
            label="Reminder"
            value={reminder}
            onChange={setReminder}
            minDate={new Date()}
          />

          {/* Subtasks */}
          <SubtaskInput
            subtasks={subtasks}
            onAddSubtask={(text) => setSubtasks([...subtasks, { id: Date.now().toString(), title: text, isCompleted: false }])}
            onRemoveSubtask={(id) => setSubtasks(subtasks.filter(s => s.id !== id))}
            onToggleSubtask={(id) => setSubtasks(subtasks.map(s => s.id === id ? { ...s, isCompleted: !s.isCompleted } : s))}
          />

          {/* Lock Toggle */}
          <LockToggle
            isLocked={isLocked}
            onToggle={handleLockToggle}
            pin={pin}
            onPinChange={setPin}
            showPinInput={showPinInput}
            onPinSubmit={handlePinSubmit}
          />
        </ScrollView>

        {/* Save Button */}
        <View style={styles.footer}>
          <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
            <LinearGradient
              colors={[COLORS.primary, COLORS.primaryLight]}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={styles.saveGradient}
            >
              <Ionicons name="checkmark-circle" size={24} color="#fff" />
              <Text style={styles.saveButtonText}>Save Note</Text>
            </LinearGradient>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  keyboardView: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  scrollContent: {
    padding: 16,
    paddingBottom: 100,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 8,
  },
  titleInput: {
    fontSize: 18,
    fontWeight: '600',
    padding: 16,
    borderRadius: 12,
    color: COLORS.text,
  },
  descriptionInput: {
    fontSize: 16,
    padding: 16,
    borderRadius: 12,
    minHeight: 150,
    color: COLORS.text,
  },
  charCount: {
    fontSize: 12,
    color: COLORS.textSecondary,
    textAlign: 'right',
    marginTop: 4,
  },
  footer: {
    padding: 16,
    backgroundColor: COLORS.card,
    borderTopWidth: 1,
    borderTopColor: COLORS.border,
  },
  saveButton: {
    borderRadius: 12,
    overflow: 'hidden',
  },
  saveGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  saveButtonText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#fff',
    marginLeft: 8,
  },
});
