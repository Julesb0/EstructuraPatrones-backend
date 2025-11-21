-- Create chat_messages table for chatbot conversations
CREATE TABLE IF NOT EXISTS chat_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'BOT')),
    content TEXT NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('LEGAL', 'FINANCE', 'MARKETING', 'OTHER')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Foreign key constraint (logical, not physical for performance)
    CONSTRAINT fk_chat_messages_user_id 
        FOREIGN KEY (user_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- Create index for efficient queries by user_id and created_at
CREATE INDEX IF NOT EXISTS idx_chat_messages_user_created 
    ON chat_messages(user_id, created_at DESC);

-- Create index for queries by user_id and category
CREATE INDEX IF NOT EXISTS idx_chat_messages_user_category 
    ON chat_messages(user_id, category, created_at DESC);

-- Enable Row Level Security
ALTER TABLE chat_messages ENABLE ROW LEVEL SECURITY;

-- Create policy: Users can only see their own messages
CREATE POLICY "Users can view own messages" ON chat_messages
    FOR SELECT USING (
        auth.uid() = user_id
    );

-- Create policy: Users can insert their own messages
CREATE POLICY "Users can insert own messages" ON chat_messages
    FOR INSERT WITH CHECK (
        auth.uid() = user_id
    );

-- Create policy: Users can update their own messages (si es necesario en el futuro)
CREATE POLICY "Users can update own messages" ON chat_messages
    FOR UPDATE USING (
        auth.uid() = user_id
    );

-- Create policy: Users can delete their own messages
CREATE POLICY "Users can delete own messages" ON chat_messages
    FOR DELETE USING (
        auth.uid() = user_id
    );

-- Grant permissions to authenticated users
GRANT ALL PRIVILEGES ON chat_messages TO authenticated;
GRANT SELECT ON chat_messages TO authenticated;
GRANT INSERT ON chat_messages TO authenticated;
GRANT UPDATE ON chat_messages TO authenticated;
GRANT DELETE ON chat_messages TO authenticated;

-- Grant basic read access to anon users (por si acaso)
GRANT SELECT ON chat_messages TO anon;