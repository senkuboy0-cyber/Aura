import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, Dimensions, Pressable, Platform, UIManager } from 'react-native';
import * as MediaLibrary from 'expo-media-library';
import { useEffect, useState, useCallback } from 'react';
import { Image } from 'expo-image';
import { FlashList } from '@shopify/flash-list';
import Animated, { FadeIn, FadeOut, Layout } from 'react-native-reanimated';
import { GestureHandlerRootView } from 'react-native-gesture-handler';

const { width } = Dimensions.get('window');
const numColumns = 3;
const gap = 2;
const itemSize = (width - gap * (numColumns - 1)) / numColumns;

if (
  Platform.OS === 'android' &&
  UIManager.setLayoutAnimationEnabledExperimental
) {
  UIManager.setLayoutAnimationEnabledExperimental(true);
}

export default function App() {
  const [permissionResponse, requestPermission] = MediaLibrary.usePermissions();
  const [assets, setAssets] = useState([]);
  const [hasNextPage, setHasNextPage] = useState(true);
  const [endCursor, setEndCursor] = useState(null);
  const [selectedImage, setSelectedImage] = useState(null);

  useEffect(() => {
    if (permissionResponse && permissionResponse.status !== 'granted') {
      requestPermission();
    }
  }, []);

  const loadMedia = useCallback(async () => {
    if (!hasNextPage || permissionResponse?.status !== 'granted') return;

    try {
      const media = await MediaLibrary.getAssetsAsync({
        first: 50,
        after: endCursor,
        sortBy: ['creationTime'],
        mediaType: ['photo', 'video'],
      });

      setAssets((prev) => [...prev, ...media.assets]);
      setHasNextPage(media.hasNextPage);
      setEndCursor(media.endCursor);
    } catch (error) {
      console.error("Error loading media:", error);
    }
  }, [hasNextPage, endCursor, permissionResponse]);

  useEffect(() => {
    if (permissionResponse?.status === 'granted') {
      loadMedia();
    }
  }, [permissionResponse]);

  const renderItem = useCallback(({ item, index }) => {
    return (
      <Animated.View
        entering={FadeIn.delay(Math.min(index * 20, 500))}
        layout={Layout.springify()}
      >
        <Pressable onPress={() => setSelectedImage(item)}>
          <Image
            source={{ uri: item.uri }}
            style={{ width: itemSize, height: itemSize, marginBottom: gap }}
            contentFit="cover"
            transition={200}
            cachePolicy="memory-disk"
          />
        </Pressable>
      </Animated.View>
    );
  }, []);

  if (!permissionResponse) {
    return <View style={styles.container} />;
  }

  if (permissionResponse.status !== 'granted') {
    return (
      <View style={styles.centerContainer}>
        <Text style={styles.title}>Gallery Access Required</Text>
        <Pressable style={styles.button} onPress={requestPermission}>
          <Text style={styles.buttonText}>Grant Permission</Text>
        </Pressable>
      </View>
    );
  }

  return (
    <GestureHandlerRootView style={styles.container}>
      <StatusBar style="light" />
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Gallery</Text>
      </View>

      <View style={{ flex: 1, backgroundColor: '#000' }}>
        {assets.length > 0 ? (
          <FlashList
            data={assets}
            renderItem={renderItem}
            numColumns={numColumns}
            estimatedItemSize={itemSize}
            onEndReached={loadMedia}
            onEndReachedThreshold={0.5}
            ItemSeparatorComponent={() => <View style={{ height: gap }} />}
            showsVerticalScrollIndicator={false}
          />
        ) : (
          <View style={styles.centerContainer}>
            <Text style={{ color: '#fff' }}>Loading media...</Text>
          </View>
        )}
      </View>

      {selectedImage && (
        <Animated.View
          style={StyleSheet.absoluteFillObject}
          entering={FadeIn}
          exiting={FadeOut}
        >
          <View style={styles.fullScreenContainer}>
            <Image
              source={{ uri: selectedImage.uri }}
              style={StyleSheet.absoluteFillObject}
              contentFit="contain"
            />
            <Pressable
              style={styles.closeButton}
              onPress={() => setSelectedImage(null)}
            >
              <Text style={styles.closeText}>Close</Text>
            </Pressable>
          </View>
        </Animated.View>
      )}
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  centerContainer: {
    flex: 1,
    backgroundColor: '#000',
    alignItems: 'center',
    justifyContent: 'center',
  },
  header: {
    paddingTop: 60,
    paddingBottom: 20,
    paddingHorizontal: 20,
    backgroundColor: '#000',
  },
  headerTitle: {
    color: '#fff',
    fontSize: 28,
    fontWeight: 'bold',
    letterSpacing: 0.5,
  },
  title: {
    color: '#fff',
    fontSize: 18,
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#fff',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 8,
  },
  buttonText: {
    color: '#000',
    fontSize: 16,
    fontWeight: '600',
  },
  fullScreenContainer: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.95)',
  },
  closeButton: {
    position: 'absolute',
    top: 60,
    right: 20,
    backgroundColor: 'rgba(255,255,255,0.2)',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    zIndex: 10,
  },
  closeText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});