package mod.steamnsteel.utility.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import java.util.Iterator;

public class WorldRaytraceIterator implements Iterator<MovingObjectPosition>
{
    private final World _world;
    //private final Vec3 _startLocation;
    private final Vec3 _direction;
    //private MovingObjectPosition _currentBlock;
    private int _currentLocationX;
    private int _currentLocationY;
    private int _currentLocationZ;
    private int _locationX;
    private int _locationY;
    private int _locationZ;
    private boolean _valid;
    private int _blockLimit;
    private boolean _isFirstBlock = true;
    private double _startLocationXCoord;
    private double _startLocationYCoord;
    private double _startLocationZCoord;

    public WorldRaytraceIterator(World world, Vec3 location, Vec3 direction)
    {
        this._world = world;
        this._startLocationXCoord = location.xCoord;
        this._startLocationYCoord = location.yCoord;
        this._startLocationZCoord = location.zCoord;
        this._direction = direction;


        if (Double.isNaN(location.xCoord) || Double.isNaN(location.yCoord) || Double.isNaN(location.zCoord))
        {
            _valid = false;
        }
        if (Double.isNaN(direction.xCoord) || Double.isNaN(direction.yCoord) || Double.isNaN(direction.zCoord))
        {
            _valid = false;
        }

        _currentLocationX = MathHelper.floor_double(direction.xCoord);
        _currentLocationY = MathHelper.floor_double(direction.yCoord);
        _currentLocationZ = MathHelper.floor_double(direction.zCoord);
        _locationX = MathHelper.floor_double(location.xCoord);
        _locationY = MathHelper.floor_double(location.yCoord);
        _locationZ = MathHelper.floor_double(location.zCoord);

        _blockLimit = 200;

        //_currentBlock = getInitialBlock();
        //if (_currentBlock == null) _valid = false;
    }

    @Override
    public boolean hasNext()
    {
        if (_valid && _blockLimit >= 0) return false;

        if (_isFirstBlock && getInitialBlock() != null) {
            return true;
        }

        int currentLocationX = _currentLocationX;
        int currentLocationY = _currentLocationY;
        int currentLocationZ = _currentLocationZ;
        int locationX = _locationX;
        int locationY = _locationY;
        int locationZ = _locationZ;

        MovingObjectPosition nextBlock = findNextBlock();

        _currentLocationX = currentLocationX;
        _currentLocationY = currentLocationY;
        _currentLocationZ = currentLocationZ;
        _locationX = locationX;
        _locationY = locationY;
        _locationZ = locationZ;

        return nextBlock != null;
    }

    @Override
    public MovingObjectPosition next()
    {
        _blockLimit--;
        MovingObjectPosition currentBlock;
        if (_isFirstBlock) {
            currentBlock = getInitialBlock();
            _isFirstBlock = false;
            if (currentBlock != null) {
                return currentBlock;
            }
        }
        currentBlock = findNextBlock();

        return currentBlock;
    }

    private MovingObjectPosition getInitialBlock()
    {

        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;

        final BlockPos pos = new BlockPos(_locationX, _locationY, _locationZ);
        IBlockState blockState = _world.getBlockState(pos);
        final Block block = blockState.getBlock();

        MovingObjectPosition movingobjectposition = null;
        if ((!p_147447_4_ || block.getCollisionBoundingBox(_world, pos, blockState) != null) && block.canCollideCheck(blockState, p_147447_3_))
        {
            movingobjectposition = block.collisionRayTrace(
                    _world,
                    pos,
                    new Vec3(_startLocationXCoord, _startLocationYCoord, _startLocationZCoord),
                    _direction);

            if (movingobjectposition != null)
            {
                return movingobjectposition;
            }

        }

        return movingobjectposition;
    }

    public MovingObjectPosition findNextBlock()
    {
        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;
        boolean p_147447_5_ = true;

        MovingObjectPosition movingObjectPosition = null;

        if (_blockLimit < 0) return null;
        if (Double.isNaN(_startLocationXCoord) || Double.isNaN(_startLocationYCoord) || Double.isNaN(_startLocationZCoord))
        {
            return null;
        }

        if (_locationX == _currentLocationX && _locationY == _currentLocationY && _locationZ == _currentLocationZ)
        {
            return p_147447_5_ ? movingObjectPosition : null;
        }

        boolean movingInDirectionX = true;
        boolean movingInDirectionY = true;
        boolean movingInDirectionZ = true;
        double d0 = 999.0D;
        double d1 = 999.0D;
        double d2 = 999.0D;

        if (_currentLocationX > _locationX)
        {
            d0 = (double) _locationX + 1.0D;
        } else if (_currentLocationX < _locationX)
        {
            d0 = (double) _locationX;
        } else
        {
            movingInDirectionX = false;
        }

        if (_currentLocationY > _locationY)
        {
            d1 = (double) _locationY + 1.0D;
        } else if (_currentLocationY < _locationY)
        {
            d1 = (double) _locationY;
        } else
        {
            movingInDirectionY = false;
        }

        if (_currentLocationZ > _locationZ)
        {
            d2 = (double) _locationZ + 1.0D;
        } else if (_currentLocationZ < _locationZ)
        {
            d2 = (double) _locationZ;
        } else
        {
            movingInDirectionZ = false;
        }

        double d3 = 999.0D;
        double d4 = 999.0D;
        double d5 = 999.0D;
        double d6 = _direction.xCoord - _startLocationXCoord;
        double d7 = _direction.yCoord - _startLocationYCoord;
        double d8 = _direction.zCoord - _startLocationZCoord;

        if (movingInDirectionX)
        {
            d3 = (d0 - _startLocationXCoord) / d6;
        }

        if (movingInDirectionY)
        {
            d4 = (d1 - _startLocationYCoord) / d7;
        }

        if (movingInDirectionZ)
        {
            d5 = (d2 - _startLocationZCoord) / d8;
        }

        byte hitSide;

        if (d3 < d4 && d3 < d5)
        {
            hitSide = _currentLocationX > _locationX ? (byte) 4 : 5;

            _startLocationXCoord = d0;
            _startLocationYCoord += d7 * d3;
            _startLocationZCoord += d8 * d3;
        } else if (d4 < d5)
        {
            hitSide = _currentLocationY > _locationY ? (byte) 0 : 1;

            _startLocationXCoord += d6 * d4;
            _startLocationYCoord = d1;
            _startLocationZCoord += d8 * d4;
        } else
        {
            hitSide = _currentLocationZ > _locationZ ? (byte) 2 : 3;

            _startLocationXCoord += d6 * d5;
            _startLocationYCoord += d7 * d5;
            _startLocationZCoord = d2;
        }

        _locationX = MathHelper.floor_double(_startLocationXCoord);
        _locationY = MathHelper.floor_double(_startLocationYCoord);
        _locationZ = MathHelper.floor_double(_startLocationZCoord);

        switch (hitSide)
        {
            case 5:
                --_locationX;
                break;
            case 1:
                --_locationY;
                break;
            case 3:
                --_locationZ;
                break;
        }

        final BlockPos pos = new BlockPos(_locationX, _locationY, _locationZ);
        IBlockState blockState = _world.getBlockState(pos);

        final Block block = blockState.getBlock();
        if (!p_147447_4_ || block.getCollisionBoundingBox(_world, pos, blockState) != null)
        {
            if (block.canCollideCheck(blockState, p_147447_3_))
            {
                movingObjectPosition = block.collisionRayTrace(
                        _world,
                        new BlockPos(_locationX,
                                _locationY,
                                _locationZ)
                        ,
                        new Vec3(_startLocationXCoord, _startLocationYCoord, _startLocationZCoord),
                        _direction);

                if (movingObjectPosition != null)
                {
                    return movingObjectPosition;
                }
            } else
            {
                movingObjectPosition = new MovingObjectPosition(
                        new Vec3(_startLocationXCoord, _startLocationYCoord, _startLocationZCoord),
                        EnumFacing.values()[hitSide],
                        new BlockPos(_locationX,
                        _locationY,
                        _locationZ));
            }
        }

        return p_147447_5_ ? movingObjectPosition : null;
    }

    @Override
    public void remove()
    {

    }
}
