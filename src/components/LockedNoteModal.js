import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TextInput,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';
import { useNotes } from '../context/NotesContext';

export default function LockedNoteModal({ visible, note, onClose, onUnlock }) {
  const { verifyPin } = useNotes();
  const [pin, setPin] = useState('');
  const [error, setError] = useState('');

  const handleUnlock = async () => {
    if (pin.length !== 4) {
      setError('PIN must be 4 digits');
      return;
    }

    const isValid = await verifyPin(pin);
    if (isValid) {
      setPin('');
      setError('');
      onUnlock();
    } else {
      setError('Incorrect PIN');
      setPin('');
    }
  };

  const handleClose = () => {
    setPin('');
    setError('');
    onClose();
  };

  if (!note) return null;

  return (
    <Modal
      visible={visible}
      animationType="fade"
      transparent
      onRequestClose={handleClose}
    >
      <View style={styles.overlay}>
        <View style={styles.modal}>
          <View style={styles.iconContainer}>
            <Ionicons name="lock-closed" size={48} color={COLORS.warning} />
          </View>

          <Text style={styles.title}>Locked Note</Text>
          <Text style={styles.noteTitle}>{note.title}</Text>

          <Text style={styles.label}>Enter PIN to unlock</Text>
          <View style={styles.pinContainer}>
            {[0, 1, 2, 3].map((index) => (
              <View
                key={index}
                style={[
                  styles.pinDot,
                  pin.length > index && styles.pinDotFilled,
                ]}
              />
            ))}
          </View>

          <TextInput
            style={styles.hiddenInput}
            value={pin}
            onChangeText={(text) => {
              if (text.length <= 4 && /^\d*$/.test(text)) {
                setPin(text);
                setError('');
              }
            }}
            keyboardType="number-pad"
            maxLength={4}
            secureTextEntry
            autoFocus
          />

          {error ? <Text style={styles.error}>{error}</Text> : null}

          <TouchableOpacity
            style={[styles.unlockButton, pin.length !== 4 && styles.unlockButtonDisabled]}
            onPress={handleUnlock}
            disabled={pin.length !== 4}
          >
            <Ionicons name="key" size={20} color="#fff" />
            <Text style={styles.unlockButtonText}>Unlock</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.cancelButton} onPress={handleClose}>
            <Text style={styles.cancelButtonText}>Cancel</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.6)',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  modal: {
    backgroundColor: COLORS.card,
    borderRadius: 24,
    padding: 32,
    width: '100%',
    maxWidth: 340,
    alignItems: 'center',
  },
  iconContainer: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: COLORS.warning + '20',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.text,
    marginBottom: 8,
  },
  noteTitle: {
    fontSize: 16,
    color: COLORS.textSecondary,
    marginBottom: 24,
    textAlign: 'center',
  },
  label: {
    fontSize: 14,
    color: COLORS.textSecondary,
    marginBottom: 16,
  },
  pinContainer: {
    flexDirection: 'row',
    gap: 16,
    marginBottom: 16,
  },
  pinDot: {
    width: 16,
    height: 16,
    borderRadius: 8,
    borderWidth: 2,
    borderColor: COLORS.border,
    backgroundColor: 'transparent',
  },
  pinDotFilled: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  hiddenInput: {
    position: 'absolute',
    opacity: 0,
    height: 0,
    width: 0,
  },
  error: {
    fontSize: 14,
    color: COLORS.error,
    marginBottom: 16,
  },
  unlockButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.primary,
    paddingVertical: 14,
    paddingHorizontal: 32,
    borderRadius: 12,
    gap: 8,
    marginTop: 8,
    width: '100%',
  },
  unlockButtonDisabled: {
    backgroundColor: COLORS.border,
  },
  unlockButtonText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#fff',
  },
  cancelButton: {
    marginTop: 16,
    padding: 12,
  },
  cancelButtonText: {
    fontSize: 16,
    color: COLORS.textSecondary,
  },
});
